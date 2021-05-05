package com.forge.messageservice.repositories

import com.alphamail.plugin.api.MessageStatus
import com.forge.messageservice.entities.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository : JpaRepository<Message, Long> {
    fun findByMessageStatus(messageStatus: MessageStatus): List<Message>
}