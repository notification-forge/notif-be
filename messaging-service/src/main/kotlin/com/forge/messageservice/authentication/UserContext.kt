package com.forge.messageservice.authentication

import com.forge.messageservice.authentication.jwt.JwtApiClient
import com.forge.messageservice.authentication.jwt.JwtUser
import org.springframework.security.core.context.SecurityContextHolder
import java.time.Instant
import java.util.*

object UserContext {

    fun loggedInUser(): JwtUser {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null) {
            if (auth.principal is JwtUser) {
                return SecurityContextHolder.getContext().authentication.principal as JwtUser
            }

            throw Exception("Trying to obtain JwtUser but auth principal is ${auth.principal.javaClass.name}")
        }

        return JwtUser("SYSTEM", "SYSTEM", Date.from(Instant.now()), listOf())
    }

    fun loggedInUsername() = loggedInUser().username

    fun apiClient(): JwtApiClient {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null) {
            if (auth.principal is JwtApiClient) {
                return SecurityContextHolder.getContext().authentication.principal as JwtApiClient
            }

            throw Exception("Trying to obtain APIClient but auth principal is ${auth.principal.javaClass.name}")
        }

        throw Exception("No authenticated API Client is found in the SecurityContext")
    }
}