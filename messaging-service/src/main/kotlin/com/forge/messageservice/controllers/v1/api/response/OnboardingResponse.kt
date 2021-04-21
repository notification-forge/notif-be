package com.forge.messageservice.controllers.v1.api.response

import com.forge.messageservice.entities.Tenant

data class OnboardingResponse(
    val appCode: String,
    val appName: String,
    val apiToken: String,
    val appStatus: Tenant.AppStatus
)
