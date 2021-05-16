package com.forge.messageservice.authentication.jwt

enum class TokenType {
    USER, API_CLIENT
}

data class TokenHolder(
    val tokenType: TokenType,
    val token: String
)
