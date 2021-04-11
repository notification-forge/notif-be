package com.forge.messageservice.entity.inputs

import com.forge.messageservice.entity.Template

data class CreateTemplateInput(
    val templateName: String,
    val alertType: Template.AlertType,
    val appCode: String
)

data class UpdateTemplateInput(
    val templateId: Long,
    val templateName: String
)