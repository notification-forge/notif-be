package com.alphamail.plugin.api

class MessageDetails(
    val id: Long,
    val templateName: String,
    val templateVersionHash: String,
    val appCode: String,
    val body: String,
    val messageType: MessageType,
    val messageStatus: MessageStatus,
    val reason: String
)


enum class MessageStatus {
    PENDING, SENT, FAILED
}

enum class MessageType {
    EMAIL, TEAMS
}