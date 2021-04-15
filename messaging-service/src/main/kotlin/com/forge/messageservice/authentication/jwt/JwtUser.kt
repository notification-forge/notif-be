package com.forge.messageservice.authentication.jwt

import java.util.*

data class JwtUser(
    val username: String,
    val name: String,
    val dateIssued: Date
) {
    override fun toString(): String{
        return "JwtUser(username=$username, name=$name, dateIssued=$dateIssued)"
    }
}