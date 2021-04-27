package com.forge.messageservice.services

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.forge.messageservice.common.engines.TemplatingEngine
import com.forge.messageservice.entities.MailSettings
import com.forge.messageservice.entities.Message
import com.forge.messageservice.entities.Message.MessageStatus.*
import com.forge.messageservice.exceptions.InvalidTemplateSettingFormatException
import com.forge.messageservice.graphql.models.inputs.MessageInput
import com.forge.messageservice.repositories.MessageRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.io.IOException
import java.util.*
import javax.mail.MessagingException
import javax.mail.internet.InternetAddress
import com.fasterxml.jackson.databind.JsonMappingException as JsonMappingException1

@Service
class MessageService(
    private val messageRepository: MessageRepository,
    private val templateService: TemplateService,
    private val templateVersionService: TemplateVersionService,
    private val templatingEngine: TemplatingEngine,
    private val javaMailSender: JavaMailSender,
    private val objectMapper: ObjectMapper,
    @Value("\${app.message.retry.limit}") private val retryLimit: Int
) {

    fun getAllPendingMails(): List<Message> {
        return messageRepository.findByMessageStatus(PENDING)
    }

    fun saveMessage(messageInput: MessageInput): Message{

        val template = templateService.getTemplateByTemplateUUID(UUID.fromString(messageInput.templateUUID))
        val templateVersion = templateVersionService.findTemplateVersionsByTemplateHashAndTemplateId(messageInput.templateHash, template.id!!)

        val message = Message().apply {
            templateId = template.id
            templateVersionId = templateVersion?.templateId
            appCode = template.appCode
            content = messageInput.content
            settings = messageInput.settings
            messageType = messageInput.messageType
        }

        return messageRepository.save(message)
    }

    fun sendMail(message: Message): Message{
        val mimeMessage = javaMailSender.createMimeMessage()
        val templateVersion = templateVersionService.getTemplateVersionById(message.templateVersionId!!)
        try{
            val mailSettings = getMailSettings(message.settings)
            val templateSettings = getMailSettings(templateVersion.settings)
            val messageHelper = MimeMessageHelper(mimeMessage, mailSettings.hasAttachments!!)

            val messageContent : Map<String, Any> = objectMapper.readValue(message.content)

            val importance = replaceIfNotEmpty(mailSettings.importance, templateSettings.importance)
            val from = replaceIfNotEmpty(mailSettings.sender, templateSettings.sender)
            val to = replaceIfNotEmpty(mailSettings.recipients, templateSettings.recipients)
            val cc = replaceIfNotEmpty(mailSettings.ccRecipients, templateSettings.ccRecipients)
            val bcc = replaceIfNotEmpty(mailSettings.bccRecipients, templateSettings.bccRecipients)
            val subject = replaceIfNotEmpty(mailSettings.subject, templateSettings.subject)
            val body = templatingEngine.parseTemplate(templateVersion.body, messageContent)

            mimeMessage.setHeader("Importance", importance)
            messageHelper.setFrom(from)
            messageHelper.setTo(InternetAddress.parse(to))
            if (cc.isNotEmpty()) messageHelper.setCc(InternetAddress.parse(cc))
            if (bcc.isNotEmpty()) messageHelper.setBcc(InternetAddress.parse(bcc))
            messageHelper.setSubject(subject)
            messageHelper.setText(body, true)

            javaMailSender.send(mimeMessage)
            message.messageStatus = SENT
        } catch (e : IOException){
            message.reason = "Unable to dispatch mail ${message.id}. Reason: Invalid param provided for template: ${templateVersion.templateHash}"
            message.messageStatus = FAILED
        } catch (e : MessagingException){
            message.reason = "Unable to dispatch mail ${message.id}. Reason: Invalid mail inputs"
            message.messageStatus = FAILED
        } catch (e : MessagingException){
            retryMessage(message, "Unable to dispatch mail ${message.id}. Reason: Unable to connect to SMTP Server")
        } catch (e: Exception){
            retryMessage(message, "Unable to dispatch mail ${message.id}. Reason: Generic Exception")
        } finally {
            return messageRepository.save(message)
        }
    }

    private fun retryMessage(message: Message, reason: String){
        if (message.timesTriggered == retryLimit){
            message.reason = reason
            message.messageStatus = FAILED
        } else {
            message.timesTriggered += 1
        }
    }

    private fun getMailSettings(setting: String): MailSettings{
        try {
            return objectMapper.readValue(setting)
        } catch (e : JsonParseException){
            throw InvalidTemplateSettingFormatException("Invalid Mail Setting format")
        } catch (e : JsonMappingException1){
            throw InvalidTemplateSettingFormatException("Invalid Mail Setting format")
        }
    }

    private fun replaceIfNotEmpty(mailSettingContent: String, templateSettingContent: String): String{
        if (mailSettingContent.isEmpty()){
            return templateSettingContent
        }
        return mailSettingContent
    }
}