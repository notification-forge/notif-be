package com.forge.messageservice.authentication.jwt

import java.util.*

data class JwtApiClient(
    val applicationCode: String,
    val name: String,
    val dateIssued: Date
)