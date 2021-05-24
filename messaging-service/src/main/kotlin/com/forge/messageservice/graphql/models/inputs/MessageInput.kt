package com.forge.messageservice.graphql.models.inputs

import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Validated
data class MessageInput(
    @NotBlank
    val templateUUID: String,
    val templateDigest: String,
    @NotBlank
    val content: String,
    @NotBlank
    val settings: String
)