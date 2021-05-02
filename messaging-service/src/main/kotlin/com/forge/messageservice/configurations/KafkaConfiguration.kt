package com.forge.messageservice.configurations

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.test.EmbeddedKafkaBroker


@Configuration
@Profile("dev | test | integration-test")
open class EmbeddedKafkaConfiguration {

    @Bean
    open fun configure(): EmbeddedKafkaBroker {
        val kafkaBroker = EmbeddedKafkaBroker(1)
        kafkaBroker.kafkaPorts(9092)
        return kafkaBroker
    }

}

@Configuration
open class KafkaProducerConfig {
    @Bean
    open fun producerFactory(): ProducerFactory<String, String> {
        return DefaultKafkaProducerFactory(producerConfigs())
    }

    @Bean
    open fun producerConfigs(): Map<String, Any> {
        // See https://kafka.apache.org/documentation/#producerconfigs for more properties
        return mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
        )
    }

    @Bean
    open fun kafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory())
    }
}

@Configuration
open class KafkaConsumerConfig {

}