package com.forge.messageservice.services

import com.forge.messageservice.common.messaging.NotificationMessage
import com.forge.messageservice.common.messaging.NotificationTask
import com.forge.messageservice.common.messaging.Recipients
import com.forge.messageservice.entities.Template
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.io.Serializable

/**
 * This service queues the message in kafka for consumption by delivery workers
 */
@Service
class MessageDispatcherService(
    private val kafkaTemplate: KafkaTemplate<String, Serializable>
) {

    private val logger = KotlinLogging.logger {}

    /**
     * Queues a message in kafka to be consumed by worker dispatchers
     */
    fun enqueueMessage() {
        kafkaTemplate.send(
            "internal-message-queue", NotificationTask(
                channel = Template.AlertType.EMAIL,
                message = NotificationMessage(
                    recipients = Recipients(
                        to = listOf("")
                    ),
                    messageBody = "Hello World"
                )
            )
        )
    }

    /**
     * Listens for messages from the `internal-message-queue` and delivers them to the
     * specified channel.
     *
     * TODO: Define the a custom structure for carrying the message to be sent and its metadata.
     */
    @KafkaListener(topics = ["internal-message-queue"], groupId = "alphamail")
    fun dequeueMessageAndDispatch(task: NotificationTask) {
        logger.info { "Message received from queue => ${task.message.messageBody}" }
    }
}