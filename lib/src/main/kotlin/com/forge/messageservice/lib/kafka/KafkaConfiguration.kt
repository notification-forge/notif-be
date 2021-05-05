package com.forge.messageservice.lib.kafka

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

open class KafkaConfig(
    val brokerAddress: String,
    val groupId: String,
    val trustStoreLocation: String,
    val trustStorePassword: String,
    val keyStoreLocation: String,
    val keyStorePassword: String,
    val sslEnabled: Boolean
)

@Configuration
open class KafkaConfiguration {

    @Value("\${spring.kafka.bootstrap-servers}")
    lateinit var brokerAddress: String

    @Value("\${spring.kafka.consumer.group-id}")
    lateinit var groupId: String

    @Value("\${spring.kafka.ssl.trust-store-location}")
    lateinit var trustStoreLocation: String

    @Value("\${spring.kafka.ssl.trust-store-password}")
    lateinit var trustStorePassword: String

    @Value("\${spring.kafka.ssl.key-store-location}")
    lateinit var keyStoreLocation: String

    @Value("\${spring.kafka.ssl.key-store-password}")
    lateinit var keyStorePassword: String

    @Value("\${kafka.ssl.enabled:false}")
    var sslEnabled: Boolean = false

    @Bean
    open fun kafkaConfig(): KafkaConfig {
        return KafkaConfig(
            brokerAddress, groupId, trustStoreLocation, trustStorePassword,
            keyStoreLocation, keyStorePassword, sslEnabled
        )
    }
}

