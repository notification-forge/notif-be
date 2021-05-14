package com.forge.messageservice.services

import com.alphamail.plugin.api.MessageType
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SchedulerService(
    private val messageService: MessageService
) {
    private val logger = LoggerFactory.getLogger(SchedulerService::class.java)

    @Scheduled(cron = "0/30 * * * * *")
    fun sendingPendingMessage() {
        logger.info("Running scheduled sending pending message job")

        val pendingMessages = messageService.getAllPendingMessages()
        pendingMessages.map { message ->
            when (message.messageType) {
                MessageType.MAIL -> {
                    messageService.sendMail(message)
                }
                MessageType.TEAMS -> {

                }
            }
        }
    }
}