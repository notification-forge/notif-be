package com.forge.messageservice.entity.inputs

import com.forge.messageservice.entity.Template
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Validated
data class CreateTemplateInput(
    @NotBlank
    val name: String,
    val alertType: Template.AlertType,
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