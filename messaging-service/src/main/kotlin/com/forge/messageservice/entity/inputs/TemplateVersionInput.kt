package com.forge.messageservice.entity.inputs

import com.forge.messageservice.entity.TemplateVersion.TemplateStatus
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Validated
data class CreateTemplateVersionInput(
    @NotBlank
    val templateId: Long
)

@Validated
data class UpdateTemplateVersionInput(
    @NotBlank
    val id: Long,
    @NotBlank
    val name: String,
    val settings: String,
    val body: String,
    val status: TemplateStatus
)

@Validated
data class CloneTemplateVersionInput(
    @NotBlank
    val id: Long,
    @NotBlank
    val name: String,
    val settings: String,
    val body: String,
    val status: TemplateStatus
)