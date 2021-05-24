package com.forge.messageservice.services

import com.alphamail.plugin.api.MessageStatus.*
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.forge.messageservice.common.messaging.MailNotificationTask
import com.forge.messageservice.common.messaging.TeamsNotificationTask
import com.forge.messageservice.entities.Message
import com.forge.messageservice.entities.responses.TeamsWebhookResponse
import com.forge.messageservice.exceptions.GraphQLQueryException
import com.forge.messageservice.exceptions.MessageDoesNotExistException
import com.forge.messageservice.exceptions.MessageRetryLimitException
import com.forge.messageservice.repositories.MessageRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.io.IOException
import javax.mail.MessagingException
import javax.mail.internet.InternetAddress

@Service
class MessageService(
    private val messageRepository: MessageRepository,
    private val templateService: TemplateService,
    private val templateVersionService: TemplateVersionService,
    private val javaMailSender: JavaMailSender,
    private val objectMapper: ObjectMapper,
    @Value("\${app.message.retry.limit}") private val retryLimit: Int
) {

    @Transactional(readOnly = true)
    fun getAllPendingMessages(): List<Message> {
        return messageRepository.findByStatus(PENDING)
    }

    @Transactional(readOnly = true)
    fun getMessage(messageId: Long): Message {
        val message = messageRepository.findById(messageId)
            ?: throw MessageDoesNotExistException("Unable to find message $messageId")
        return message.get()
    }

    @Transactional(readOnly = true)
    fun getAllMessagesWithTemplateNameAndInAppCode(
        appCode: String,
        name: String,
        pageable: Pageable,
        sortField: String
    ): Page<Message> {
        try {
            val template = templateService.getTemplateByTemplateNameAndAppCode(name, appCode)
            return messageRepository.findWithNamesLike(appCode, template.id!!, pageable)
        } catch (e: Exception) {
            throw GraphQLQueryException("sortField: $sortField is invalid")
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    fun saveMessage(message: Message): Message {
        return messageRepository.save(message)
    }

    fun sendMessage(task: TeamsNotificationTask): Message {
        val teamSettings = task.teamSettings
        val message = getMessage(task.messageId)
        ensureRetryable(message.timesTriggered)
        try {
            val request = HttpEntity(task.body)
            val restTemplate = RestTemplate()
            restTemplate.exchange(
                teamSettings.teamsWebhookUrl!!,
                HttpMethod.POST,
                request,
                TeamsWebhookResponse::class.java
            )
            message.reason = ""
            message.status = SENT
        } catch (e: JsonProcessingException) {
            message.reason =
                "Unable to dispatch teams message ${message.id}. Reason: Invalid param provided for templates"
            message.status = FAILED
        } catch (e: Exception) {
            retryMessage(
                message,
                "Unable to dispatch teams message ${message.id}. Reason: Invalid param provided for templates"
            )
        } finally {
            return messageRepository.save(message)
        }
    }

    fun sendMessage(task: MailNotificationTask): Message {
        val mimeMessage = javaMailSender.createMimeMessage()
        val mailSettings = task.mailSettings
        val message = getMessage(task.messageId)
        ensureRetryable(message.timesTriggered)
        try {
            val messageHelper = MimeMessageHelper(mimeMessage, false)
            mimeMessage.setHeader("Importance", mailSettings.importance)
            messageHelper.setFrom(mailSettings.sender)
            messageHelper.setTo(InternetAddress.parse(mailSettings.recipients))
            if (mailSettings.ccRecipients.isNotEmpty()) messageHelper.setCc(InternetAddress.parse(mailSettings.ccRecipients))
            if (mailSettings.bccRecipients.isNotEmpty()) messageHelper.setBcc(InternetAddress.parse(mailSettings.bccRecipients))
            messageHelper.setSubject(mailSettings.subject)
            messageHelper.setText(task.body, true)

            javaMailSender.send(mimeMessage)
            message.status = SENT
        } catch (e: IOException) {
            message.reason = "Unable to dispatch mail ${message.id}. Reason: Invalid param provided for template"
            message.status = FAILED
        } catch (e: MessagingException) {
            message.reason = "Unable to dispatch mail ${message.id}. Reason: Invalid mail inputs"
            message.status = FAILED
        } catch (e: MailException) {
            retryMessage(message, "Unable to dispatch mail ${message.id}. Reason: Unable to connect to SMTP Server")
        } catch (e: Exception) {
            retryMessage(message, "Unable to dispatch mail ${message.id}. Reason: Generic Exception")
        } finally {
            return messageRepository.save(message)
        }
    }

    private fun ensureRetryable(timesTriggered: Int) {
        if (timesTriggered >= retryLimit) {
            throw MessageRetryLimitException("Message retry limit has met")
        }
    }

    private fun retryMessage(message: Message, reason: String) {
        message.timesTriggered += 1
        if (message.timesTriggered == retryLimit) {
            message.reason = reason
            message.status = FAILED
        }
    }
}