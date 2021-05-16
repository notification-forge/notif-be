package com.forge.messageservice.controllers.v1.api.request

import com.forge.messageservice.common.messaging.Recipients

data class MessageDispatchRequest(
    val recipients: Recipients,
    val subject: String?,
    val parameters: Map<String, Any>
)