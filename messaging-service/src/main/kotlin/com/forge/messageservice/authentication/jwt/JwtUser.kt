package com.forge.messageservice.authentication.jwt

import com.forge.messageservice.controllers.v1.api.response.OnboardingResponse
import java.util.*

data class JwtUser(
    val username: String,
    val name: String,
    val dateIssued: Date,
    var apps: List<OnboardingResponse>
) {
    override fun toString(): String{
        return "JwtUser(username=$username, name=$name, dateIssued=$dateIssued, apps=$apps)"
    }
}