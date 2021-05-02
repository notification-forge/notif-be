package com.forge.messageservice.services

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
open class MessageDispatcherService(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {

    /**
     * Queues a message in kafka to be consumed by worker dispatchers
     */
    fun dispatch() {
        kafkaTemplate.send("internal-message-queue", "Hello World")
    }

}