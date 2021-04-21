package com.forge.messageservice.entities.inputs

import com.forge.messageservice.entities.Tenant
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Validated
data class CreateAppInput(
    @NotBlank
    val appCode: String,
    @NotBlank
    val name: String,
    val justification: String,
    val description: String,
    val primaryOwnerName: String,
    val primaryOwnerId: String,
    val secondaryOwnerName: String,
    val secondaryOwnerId: String
)

@Validated
data class UpdateAppInput(
    @NotBlank
    val appCode: String,
    val name: String,
    val justification: String,
    val description: String,
    val primaryOwnerName: String,
    val primaryOwnerId: String,
    val secondaryOwnerName: String,
    val secondaryOwnerId: String
)

@Validated
data class ApprovalAppInput(
    @NotBlank
    val appCode: String,
    val status: Tenant.AppStatus,
    val rejectedReason: String
)
