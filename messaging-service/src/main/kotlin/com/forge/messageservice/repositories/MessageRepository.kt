package com.forge.messageservice.repositories

import com.alphamail.plugin.api.MessageStatus
import com.forge.messageservice.entities.Message
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MessageRepository : JpaRepository<Message, Long> {
    fun findByStatus(status: MessageStatus): List<Message>

    @Query("SELECT m FROM Message m WHERE m.templateId = :templateId AND m.appCode = :appCode")
    fun findWithNamesLike(appCode: String, templateId: Long, pageable: Pageable): Page<Message>
}