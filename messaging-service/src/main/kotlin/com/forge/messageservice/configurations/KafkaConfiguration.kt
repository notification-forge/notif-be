package com.forge.messageservice.configurations

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
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