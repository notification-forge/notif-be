package com.forge.messageservice.common.messaging

import java.io.Serializable

data class Recipients(
    val to: List<String>,
    val cc: List<String> = emptyList(),
    val bb: List<String> = emptyList()
) : Serializable

data class NotificationMessage(
    val recipients: Recipients,
    val messageBody: String
) : Serializable