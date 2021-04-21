package com.forge.messageservice.authentication

import com.forge.messageservice.authentication.jwt.JwtUser
import org.springframework.security.core.context.SecurityContextHolder
import java.time.Instant
import java.util.*

object UserContext {

    fun loggedInUser(): JwtUser{
        if (SecurityContextHolder.getContext().authentication != null)
            return SecurityContextHolder.getContext().authentication.principal as JwtUser
        return JwtUser("SYSTEM", "SYSTEM", Date.from(Instant.now()), listOf())
    }

    fun loggedInUsername() = loggedInUser().username
}