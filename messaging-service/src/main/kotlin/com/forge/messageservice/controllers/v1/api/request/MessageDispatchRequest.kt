package com.forge.messageservice.controllers.v1.api.request

data class MessageDispatchRequest(
    val settings: String,
    val parameters: String
)