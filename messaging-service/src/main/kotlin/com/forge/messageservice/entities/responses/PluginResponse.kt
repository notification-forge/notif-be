package com.forge.messageservice.entities.responses

import com.alphamail.plugin.api.FieldType
import java.time.LocalDateTime

data class PluginResponse(
    private val id: Long,
    private val name: String,
    private val appCode: String?,
    private val configurations: List<Configuration>,
    private val createdDate: LocalDateTime?,
    private val createdBy: String?,
    private val lastModifiedDate: LocalDateTime?,
    private val lastModifiedBy: String?
)

data class Configuration(
    private val name: String,
    private val displayName: String,
    private val fieldType: FieldType,
    private val description: String,
    private val mandatory: Boolean,
    private val allowedOptions: List<String>,
    private val validationExpr: String,
    private val value: String? = null
)