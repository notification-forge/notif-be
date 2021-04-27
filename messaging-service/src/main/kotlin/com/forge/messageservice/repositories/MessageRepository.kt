package com.forge.messageservice.repositories

import com.forge.messageservice.entities.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository : JpaRepository<Message, Long>{
    fun findByMessageStatus(messageStatus: Message.MessageStatus): List<Message>
}