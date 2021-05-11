package com.forge.messageservice.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.test.EmbeddedKafkaBroker


@Configuration
@Profile("dev | test | integration-test")
class EmbeddedKafkaConfiguration {

    @Bean
    fun configure(): EmbeddedKafkaBroker {
        val kafkaBroker = EmbeddedKafkaBroker(1)
        kafkaBroker.kafkaPorts(9092)
        return kafkaBroker
    }

}