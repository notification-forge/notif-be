package com.forge.messageservice.entities.responses

import com.alphamail.plugin.api.FieldType

data class PluginResponse(
    private val id: Long,
    private val name: String,
    private val appCode: String?,
    private val configurationDescriptors: List<ConfigurationDescriptor>
)

data class ConfigurationDescriptor(
    private val name: String,
    private val displayName: String,
    private val fieldType: FieldType,
    private val description: String,
    private val mandatory: Boolean,
    private val allowedOptions: List<String>,
    private val validationExpr: String
)