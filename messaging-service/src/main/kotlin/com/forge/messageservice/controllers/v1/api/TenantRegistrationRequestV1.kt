package com.forge.messageservice.controllers.v1.api

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class TenantRegistrationRequestV1(

    @NotNull
    @NotEmpty
    val appCode: String = "",

    @NotNull
    @NotEmpty
    val displayName: String = "",

    @NotNull
    @NotEmpty
    val requesterEmailAddress: String? = null,

    @NotNull
    @NotEmpty
    val primaryOwnerEmailAddress: String? = null,

    @NotNull
    @NotEmpty
    val secondaryOwnerEmailAddress: String? = null
)

