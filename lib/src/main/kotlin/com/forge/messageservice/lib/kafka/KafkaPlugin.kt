package com.forge.messageservice.lib.kafka

import com.alphamail.plugin.api.AlphamailPlugin
import com.alphamail.plugin.api.MessageDetails
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate

class KafkaPlugin(
    private val cfg: String
) : AlphamailPlugin {

    private val logger = LoggerFactory.getLogger(KafkaPlugin::class.java)
    private val kafkaTemplate = SpringBeanUtil.getBean("kafkaTemplate") as KafkaTemplate<String, String>
    private val objectMapper = ObjectMapper()

    override fun beforeSend(message: MessageDetails): Any {
        val configuration : KafkaPluginConfiguration = objectMapper.readValue(cfg)

        val message = "This is to inform you that your message. " +
                "Message Type: ${message.messageType}, Message Id: ${message.id}, " +
                "Template Name: ${message.templateName}, Template Version Hash: ${message.templateVersionHash} " +
                "has a status of ${message.messageStatus}"
        kafkaTemplate.send(configuration.topic, message)

        return "This is kafka plugin before sending, for ${configuration.topic}"
    }

    override fun afterSend(message: MessageDetails): Any {
        return "This is kafka plugin after sending"
    }
}