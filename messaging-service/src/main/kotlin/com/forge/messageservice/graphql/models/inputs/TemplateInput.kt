package com.forge.messageservice.graphql.models.inputs

import com.alphamail.plugin.api.MessageType
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Validated
data class CreateTemplateInput(
    @NotBlank
    val name: String,
    val alertType: MessageType,
    @NotBlank
    val appCode: String
)

@Validated
data class UpdateTemplateInput(
    @NotBlank
    val id: Long,
    @NotBlank
    val name: String
)