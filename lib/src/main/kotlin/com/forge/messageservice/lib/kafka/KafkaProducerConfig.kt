package com.forge.messageservice.lib.kafka

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
open class KafkaTemplateConfig {

    @Autowired
    lateinit var producerFactory: ProducerFactory<String, String>

    @Bean
    open fun kafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory)
    }
}

@Configuration
open class KafkaProducerFactoryConfig {

    @Bean
    open fun producerFactory(kafkaConfig: KafkaConfig): ProducerFactory<String, String> {
        val props = mutableMapOf<String, Any>(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaConfig.brokerAddress,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name
        )
        if (kafkaConfig.sslEnabled) {
            val sslMap = mapOf<String, Any>(
                SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG to kafkaConfig.trustStoreLocation,
                SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG to kafkaConfig.trustStorePassword,
                SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG to kafkaConfig.keyStoreLocation,
                SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG to kafkaConfig.keyStorePassword,
                SslConfigs.SSL_KEY_PASSWORD_CONFIG to kafkaConfig.keyStorePassword,
                SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG to "",
                "security.protocol" to "SSL"
            )
            props.putAll(sslMap)
        }
        return DefaultKafkaProducerFactory(props)
    }
}