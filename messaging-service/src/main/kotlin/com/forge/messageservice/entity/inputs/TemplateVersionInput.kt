package com.forge.messageservice.entity.inputs

import com.forge.messageservice.entity.TemplateVersion.TemplateStatus

data class CreateTemplateVersionInput(
    val templateId: Long
)

data class UpdateTemplateVersionInput(
    val templateVersionId: Long,
    val templateVersionName: String,
    val templateId: Long,
    val settings: String,
    val body: String,
    val templateStatus: TemplateStatus,
    val teamsWebhookId: Long
)