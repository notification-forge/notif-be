package com.forge.messageservice.entities.responses

import com.alphamail.plugin.api.MessageStatus
import com.alphamail.plugin.api.MessageType

data class MessageResponse(
    private val id: Long,
    private val templateName: String,
    private val templateDigest: String,
    private val type: MessageType,
    private val status: MessageStatus
)