package com.forge.messageservice.services

import com.alphamail.plugin.api.MessageStatus
import com.alphamail.plugin.api.MessageType
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.forge.messageservice.common.engines.TemplatingEngine
import com.forge.messageservice.common.messaging.MailNotificationTask
import com.forge.messageservice.common.messaging.TeamsNotificationTask
import com.forge.messageservice.controllers.v1.api.request.MessageDispatchRequest
import com.forge.messageservice.entities.MailSettings
import com.forge.messageservice.entities.Message
import com.forge.messageservice.entities.TeamsSettings
import com.forge.messageservice.exceptions.InvalidTemplateSettingFormatException
import com.forge.messageservice.exceptions.MessageRetryLimitException
import com.forge.messageservice.graphql.models.inputs.MessageInput
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service
import java.io.Serializable

/**
 * This service queues the message in kafka for consumption by delivery workers
 */
@Service
class MessageDispatcherService(
    private val kafkaTemplate: KafkaTemplate<String, Serializable>,
    private val messageValidationService: MessageValidationService,
    private val messageService: MessageService,
    private val templateVersionService: TemplateVersionService,
    private val templatingEngine: TemplatingEngine,
    private val objectMapper: ObjectMapper
) {

    private val logger = KotlinLogging.logger {}

    fun dispatchMessage(messageInput: MessageInput): Message {
        val message = messageService.saveMessage(messageValidationService.generateMessage(messageInput))
        enqueueMessage(message)
        return message
    }

    fun dispatchMessage(
        templateUUID: String,
        templateDigest: String,
        messageDispatchRequest: MessageDispatchRequest
    ): Message {
        val message = messageService.saveMessage(
            messageValidationService.generateMessage(
                templateUUID,
                templateDigest,
                messageDispatchRequest
            )
        )
        enqueueMessage(message)
        return message
    }

    fun enqueueMessage(message: Message) {
        val templateVersion = templateVersionService.getTemplateVersion(message.templateVersionId!!)
        val settings = generateSettings(message.settings, templateVersion.settings)
        when (message.type) {
            MessageType.EMAIL -> {
                val mailSettings: MailSettings = objectMapper.readValue(settings)
                enqueueMailMessage(
                    MailNotificationTask(
                        message.id!!,
                        mailSettings,
                        generateMessageBody(templateVersion.body, message.content)
                    )
                )
            }
            MessageType.TEAMS -> {
                val teamSettings: TeamsSettings = objectMapper.readValue(settings)
                enqueueTeamsMessage(
                    TeamsNotificationTask(
                        message.id!!,
                        teamSettings,
                        generateMessageBody(templateVersion.body, message.content)
                    )
                )
            }
        }
    }

    /**
     * Queues an email message in kafka to be consumed by worker dispatchers
     */
    fun enqueueMailMessage(task: MailNotificationTask) {
        kafkaTemplate.send("internal-mail-message-queue", task)
    }

    /**
     * Queues a teams message in kafka to be consumed by worker dispatchers
     */
    fun enqueueTeamsMessage(task: TeamsNotificationTask) {
        kafkaTemplate.send("internal-teams-message-queue", task)
    }


    /**
     * Listens for messages from the `internal-message-queue` and delivers them to the
     * specified channel.
     *
     * TODO: Define the a custom structure for carrying the message to be sent and its metadata.
     */
    @KafkaListener(topics = ["internal-mail-message-queue"], groupId = "alphamail")
    fun dequeueMailMessageAndDispatch(task: MailNotificationTask, ack: Acknowledgment) {
        logger.info { "Message received from queue => ${task.messageId}" }
        try {
            val message = messageService.sendMessage(task)
            if (message.status == MessageStatus.SENT) {
                ack.acknowledge()
            }
        } catch (e: MessageRetryLimitException) {
            ack.acknowledge()
        }
    }

    /**
     * Listens for messages from the `internal-message-queue` and delivers them to the
     * specified channel.
     *
     * TODO: Define the a custom structure for carrying the message to be sent and its metadata.
     */
    @KafkaListener(topics = ["internal-teams-message-queue"], groupId = "alphamail")
    fun dequeueTeamsMessageAndDispatch(task: TeamsNotificationTask, ack: Acknowledgment) {
        logger.info { "Message received from queue => ${task.messageId}" }
        try {
            val message = messageService.sendMessage(task)
            if (message.status == MessageStatus.SENT) {
                ack.acknowledge()
            }
        } catch (e: MessageRetryLimitException) {
            ack.acknowledge()
        }
    }

    private fun generateSettings(messageSetting: String, templateSetting: String): String {
        return messageValidationService.generateSettings(
            Settings(
                getSettingsMap(messageSetting),
                getSettingsMap(templateSetting),
                MessageType.EMAIL
            )
        )
    }

    private fun getSettingsMap(setting: String): Map<String, String> {
        try {
            return objectMapper.readValue(setting)
        } catch (e: JsonParseException) {
            throw InvalidTemplateSettingFormatException("Invalid Mail Setting format")
        } catch (e: JsonMappingException) {
            throw InvalidTemplateSettingFormatException("Invalid Mail Setting format")
        }
    }

    private fun generateMessageBody(body: String, content: String): String {
        val messageContent: Map<String, Any> = objectMapper.readValue(content)
        return templatingEngine.parseTemplate(body, messageContent)
    }
}