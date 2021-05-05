package com.forge.messageservice.graphql.models.inputs

import com.alphamail.plugin.api.MessageType
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Validated
data class MessageInput(
    @NotBlank
    val templateUUID: String,
    val templateHash: Int,
    @NotBlank
    val content: String,
    @NotBlank
    val settings: String,
    @NotNull
    val messageType: MessageType
)