package com.forge.messageservice.services

import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

/**
 * This service queues the message in kafka for consumption by delivery workers
 */
@Service
open class MessageDispatcherService(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {

    private val logger = KotlinLogging.logger {}

    /**
     * Queues a message in kafka to be consumed by worker dispatchers
     */
    fun enqueueMessage() {
        kafkaTemplate.send("internal-message-queue", "Hello World")
    }

    /**
     * Listens for messages from the `internal-message-queue` and delivers them to the
     * specified channel.
     *
     * TODO: Define the a custom structure for carrying the message to be sent and its metadata.
     */
    @KafkaListener(topics = ["internal-message-queue"], groupId = "alphamail")
    fun dequeueMessageAndDispatch(message: String) {
        logger.info { "Message received from queue => $message" }
    }
}