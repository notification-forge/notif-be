package com.forge.messageservice.services.aspects

import com.alphamail.plugin.api.MessageDetails
import com.alphamail.plugin.api.PluginConfiguration
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.forge.messageservice.common.engines.TemplatingEngine
import com.forge.messageservice.entities.Message
import com.forge.messageservice.entities.TemplatePlugin
import com.forge.messageservice.entities.TemplateVersion
import com.forge.messageservice.services.PluginService
import com.forge.messageservice.services.TemplatePluginService
import com.forge.messageservice.services.TemplateVersionService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class EmailReviewAspect(
    private val templateVersionService: TemplateVersionService,
    private val templatePluginService: TemplatePluginService,
    private val pluginService: PluginService,
    private val templatingEngine: TemplatingEngine,
    private val objectMapper: ObjectMapper
) {

    companion object {
        private val logger = LoggerFactory.getLogger(EmailReviewAspect::class.java)
    }

    @Before("execution(* com.forge.messageservice.services.MessageService.sendMail(..)) ")
    fun preProcess(joinPoint: JoinPoint) {
        val message = joinPoint.args[0] as Message
        logger.info("(START) Pre Processing Actions for ${message.id}")

        val templateVersion = templateVersionService.getTemplateVersionById(message.templateVersionId!!)
        val templatePlugins = templatePluginService.getTemplatePluginsByTemplateVersionId(templateVersion.id!!)

        templatePlugins.map { templatePlugin ->
            val plugin = pluginService.loadPlugin(templatePlugin.plugin!!, templatePlugin.configuration!!)
            try {
                logger.info("Processing ${plugin.javaClass}")
                plugin.beforeSend(convertToMessageDetails(message, templateVersion))
            } catch (e: Exception) {
                logger.error("Unable to execute ${plugin.javaClass}")
            }
            logger.info("Processed ${plugin.javaClass}")
        }

        logger.info("(COMPLETE) Pre Processing Actions for ${message.id}")
    }

    @AfterReturning("execution(* com.forge.messageservice.services.MessageService.sendMail(..)) ", returning = "retVal")
    fun postProcess(retVal: Any) {
        val message = retVal as Message
        logger.info("(START) Post Processing Actions for ${message.id}")

        val templateVersion = templateVersionService.getTemplateVersionById(message.templateVersionId!!)
        val templatePlugins = templatePluginService.getTemplatePluginsByTemplateVersionId(templateVersion.id!!)

        templatePlugins.map { templatePlugin ->
            val plugin = pluginService.loadPlugin(templatePlugin.plugin!!, templatePlugin.configuration!!)
            try {
                logger.info("Processing ${plugin.javaClass}")
                plugin.afterSend(convertToMessageDetails(message, templateVersion))
            } catch (e: Exception) {
                logger.error("Unable to execute ${plugin.javaClass}")
            }
            logger.info("Processed ${plugin.javaClass}")
        }

        logger.info("(COMPLETE) Post Processing Actions for ${message.id}")
    }

    private fun convertToMessageDetails(message: Message, templateVersion: TemplateVersion): MessageDetails {
        val messageContent: Map<String, Any> = objectMapper.readValue(message.content)
        return MessageDetails(
            message.id!!,
            templateVersion.template!!.name!!,
            templateVersion.templateHash!!,
            message.appCode!!,
            templatingEngine.parseTemplate(templateVersion.body, messageContent),
            message.messageType,
            message.messageStatus,
            message.reason!!
        )
    }
}