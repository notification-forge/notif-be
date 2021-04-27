package com.forge.messageservice.resolvers.queries.responses

import com.alphamail.plugin.api.FieldConfiguration
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.forge.messageservice.entities.Plugin
import com.forge.messageservice.entities.TemplatePlugin
import com.forge.messageservice.entities.responses.Configuration
import com.forge.messageservice.entities.responses.PluginResponse
import com.forge.messageservice.services.TemplatePluginService
import org.springframework.stereotype.Component

@Component
class PluginResponseConverter(
    private val objectMapper: ObjectMapper,
    private val templatePluginService: TemplatePluginService
) {

    fun convertToPluginResponse(plugin: Plugin): PluginResponse {
        val fieldConfigurations: List<FieldConfiguration> = objectMapper.readValue(plugin.configurationDescriptors!!)
        return PluginResponse(
            plugin.id!!,
            plugin.name!!,
            plugin.appCode,
            convertToConfiguration(fieldConfigurations),
            plugin.createdDate,
            plugin.createdBy,
            plugin.lastModifiedDate,
            plugin.lastModifiedBy
        )
    }

    fun convertToPluginResponse(templateVersionId: Long, plugin: Plugin): PluginResponse {
        val fieldConfigurations: List<FieldConfiguration> = objectMapper.readValue(plugin.configurationDescriptors!!)
        return PluginResponse(
            plugin.id!!,
            plugin.name!!,
            plugin.appCode,
            convertToConfiguration(fieldConfigurations, templatePluginService
                .getTemplatePluginsByTemplateVersionIdAndPluginId(templateVersionId, plugin.id!!)),
            plugin.createdDate,
            plugin.createdBy,
            plugin.lastModifiedDate,
            plugin.lastModifiedBy
        )
    }

    private fun convertToConfiguration(fieldConfigurations: List<FieldConfiguration>): List<Configuration> {
        return fieldConfigurations.map { fieldConfiguration ->
            Configuration(
                fieldConfiguration.name,
                fieldConfiguration.displayName,
                fieldConfiguration.fieldType,
                fieldConfiguration.description,
                fieldConfiguration.mandatory,
                fieldConfiguration.allowedOptions.toList(),
                fieldConfiguration.validationExpr
            )
        }
    }

    private fun convertToConfiguration(
        fieldConfigurations: List<FieldConfiguration>,
        templatePlugin: TemplatePlugin
    ): List<Configuration> {
        val configurationsMap: Map<String, String> = objectMapper.readValue(templatePlugin.configuration!!)
        return fieldConfigurations.map { fieldConfiguration ->
            Configuration(
                fieldConfiguration.name,
                fieldConfiguration.displayName,
                fieldConfiguration.fieldType,
                fieldConfiguration.description,
                fieldConfiguration.mandatory,
                fieldConfiguration.allowedOptions.toList(),
                fieldConfiguration.validationExpr,
                configurationsMap[fieldConfiguration.name]
            )
        }
    }
}