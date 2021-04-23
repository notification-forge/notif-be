package com.forge.messageservice.entities.inputs

import com.forge.messageservice.entities.TemplateVersion.TemplateStatus
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
    val status: TemplateStatus,
    val plugins: PluginsInput
)

@Validated
data class CloneTemplateVersionInput(
    @NotBlank
    val templateId: Long,
    @NotBlank
    val name: String,
    val settings: String,
    val body: String,
    val plugins: PluginsInput
)