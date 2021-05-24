package com.forge.messageservice.services

import com.alphamail.plugin.api.MessageType
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.forge.messageservice.controllers.v1.api.request.MessageDispatchRequest
import com.forge.messageservice.entities.Message
import com.forge.messageservice.entities.Template
import com.forge.messageservice.entities.TemplateVersion
import com.forge.messageservice.exceptions.InvalidMessageSettingException
import com.forge.messageservice.graphql.models.inputs.MessageInput
import org.springframework.stereotype.Service
import java.util.*

data class Settings(
    val messageSettings: Map<String, String>,
    val templateSettings: Map<String, String>,
    val messageType: MessageType
)

data class MessageValidation(
    val template: Template,
    val templateVersion: TemplateVersion,
    val content: String,
    val settings: String
)

@Service
class MessageValidationService(
    private val templateService: TemplateService,
    private val templateVersionService: TemplateVersionService,
    private val templateCfgService: TemplateCfgService,
    private val objectMapper: ObjectMapper
) {

    fun generateMessage(
        templateUUID: String,
        templateDigest: String,
        messageDispatchRequest: MessageDispatchRequest
    ): Message {
        val template = templateService.getTemplateByTemplateUUID(UUID.fromString(templateUUID))
        val templateVersion = templateVersionService.getTemplateVersion(templateDigest, template.id!!)

        return validateAndCreateMessage(
            MessageValidation(
                template, templateVersion, messageDispatchRequest.parameters, messageDispatchRequest.settings
            )
        )
    }

    fun generateMessage(messageInput: MessageInput): Message {
        val templateUUID = UUID.fromString(messageInput.templateUUID)
        val template = templateService.getTemplateByTemplateUUID(templateUUID)
        val templateVersion = templateVersionService.getTemplateVersion(messageInput.templateDigest, template.id!!)

        return validateAndCreateMessage(
            MessageValidation(
                template, templateVersion, messageInput.content, messageInput.settings
            )
        )
    }

    private fun validateAndCreateMessage(messageValidation: MessageValidation): Message {
        validateMessage(messageValidation)
        return createMessage(messageValidation)
    }

    private fun validateMessage(messageValidation: MessageValidation) {
        val messageSettings = validateAndConvertToMap(messageValidation.settings)
        validateAndConvertToMap(messageValidation.content)

        validateSettings(
            Settings(
                messageSettings,
                validateAndConvertToMap(messageValidation.settings),
                messageValidation.template.type
            )
        )
    }

    private fun createMessage(messageValidation: MessageValidation): Message {
        val template = messageValidation.template
        return Message().apply {
            templateId = template.id
            templateVersionId = messageValidation.templateVersion.id
            appCode = template.appCode
            this.content = messageValidation.content
            this.settings = messageValidation.settings
            type = template.type
        }
    }

    private fun validateSettings(settings: Settings) {
        val templateCfgDetails = templateCfgService.findCfgDetailsFor(settings.messageType)

        templateCfgDetails.map { templateCfgDetail ->
            val fieldName = templateCfgDetail.fieldName

            if (templateCfgDetail.isMandatory) {
                val messageValue = settings.messageSettings[fieldName] ?: ""
                val templateValue = settings.templateSettings[fieldName] ?: ""
                ensureNotBlank(fieldName, messageValue, templateValue)
            }
        }
    }

    private fun validateAndConvertToMap(value: String): Map<String, String> {
        try {
            return objectMapper.readValue(value)
        } catch (e: JsonProcessingException) {
            throw InvalidMessageSettingException("Invalid settings or content format")
        } catch (e: JsonMappingException) {
            throw InvalidMessageSettingException("Invalid settings or content format")
        }
    }

    private fun ensureNotBlank(fieldName: String, messageValue: String, templateValue: String) {
        if (messageValue.isEmpty() and templateValue.isEmpty()) {
            throw InvalidMessageSettingException("$fieldName is mandatory in settings.")
        }
    }

    fun generateSettings(settings: Settings): String {
        val templateCfgDetails = templateCfgService.findCfgDetailsFor(settings.messageType)
        val messageSettings = settings.messageSettings
        val templateSettings = settings.templateSettings

        val mailSettings = mutableMapOf<String, Any>()

        templateCfgDetails.map { templateCfgDetail ->
            val fieldName = templateCfgDetail.fieldName
            val mailSettingContent = messageSettings[fieldName] ?: ""
            val templateSettingContent = templateSettings[fieldName] ?: ""
            val content = replaceIfNotEmpty(mailSettingContent, templateSettingContent)
            mailSettings[fieldName] = content
        }
        return objectMapper.writeValueAsString(mailSettings)
    }

    private fun replaceIfNotEmpty(mailSettingContent: String, templateSettingContent: String): String {
        if (mailSettingContent.isEmpty()) {
            return templateSettingContent
        }
        return mailSettingContent
    }


}