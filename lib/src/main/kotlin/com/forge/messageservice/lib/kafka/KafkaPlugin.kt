package com.forge.messageservice.lib.kafka

import com.alphamail.plugin.api.AlphamailPlugin
import mu.KotlinLogging
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFutureCallback

/**
 * Kafka plugin drops a message to a kafka topic indicating that a message has been
 * sent.
 *
 * This class acts as a wrapper and delegates the actual message sending to [KafkaMessageProducer] to that
 * unit test can be written for [KafkaMessageProducer]
 */
class KafkaPlugin(cfg: KafkaConfiguration) : AlphamailPlugin {

    private val logger = KotlinLogging.logger {}

    private val kafkaTemplate: KafkaTemplate<String, String>
    private val msgProducer: KafkaMessageProducer

    private val handler = object : ListenableFutureCallback<SendResult<String, String>> {

        override fun onSuccess(result: SendResult<String, String>?) {
            logger.info { "Kafka message sent ${result?.recordMetadata?.offset()}" }
        }

        override fun onFailure(ex: Throwable) {
            logger.error { ex }
        }
    }

    init {
        val defaultKafkaProducerFactory = DefaultKafkaProducerFactory<String, String>(
            mapOf(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to cfg.bootStrapAddress,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::javaClass,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::javaClass
            )
        )
        kafkaTemplate = KafkaTemplate(defaultKafkaProducerFactory)
        msgProducer = KafkaMessageProducer(cfg, kafkaTemplate, handler)
    }

    override fun execute(): Any? {
        // TODO: Update `execute()` signature to pass the message to be sent
        msgProducer.send("")
        return null
    }

    override fun type(): AlphamailPlugin.PluginType {
        return AlphamailPlugin.PluginType.AfterSend
    }
}