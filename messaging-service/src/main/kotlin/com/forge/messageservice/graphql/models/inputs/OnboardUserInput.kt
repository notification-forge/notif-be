package com.forge.messageservice.graphql.models.inputs

import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Validated
data class OnboardUserInput(
    @NotBlank
    val appCode: String,
    @NotBlank
    val username: String,
    val name: String
)