package com.forge.messageservice.lib.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.listener.ContainerProperties

@Configuration
open class KafkaConsumerConfig {

    @Autowired
    lateinit var kafkaConfig: KafkaConfig

    @Bean
    open fun consumerFactory(): ConsumerFactory<String, String> {
        val props = mutableMapOf<String, Any>(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaConfig.brokerAddress,
            ConsumerConfig.MAX_POLL_RECORDS_CONFIG to 1,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false,
            ConsumerConfig.GROUP_ID_CONFIG to kafkaConfig.groupId
        )
        if (kafkaConfig.sslEnabled) {
            val sslMap = mapOf(
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
        return DefaultKafkaConsumerFactory(props)
    }
}

@Configuration
open class KafkaListenerContainerConfig {

    @Autowired
    lateinit var kafkaConsumerFactory: ConsumerFactory<String, String>

    @Bean
    open fun kafkaListenerContainerFactory(): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.setConcurrency(1)
        factory.consumerFactory = kafkaConsumerFactory
        factory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL_IMMEDIATE
        return factory
    }
}