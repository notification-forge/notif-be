package com.forge.messageservice.controllers.v1.api.request

data class LoginRequest(
    val username: String,
    val password: String
)