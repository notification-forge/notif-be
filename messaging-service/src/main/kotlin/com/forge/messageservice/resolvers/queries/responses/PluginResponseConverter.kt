package com.forge.messageservice.resolvers.queries.responses

import com.alphamail.plugin.api.FieldConfiguration
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.forge.messageservice.entities.Plugin
import com.forge.messageservice.entities.responses.ConfigurationDescriptor
import com.forge.messageservice.entities.responses.PluginResponse
import com.forge.messageservice.services.PluginService
import org.springframework.stereotype.Component

@Component
class PluginResponseConverter(
    private val objectMapper: ObjectMapper
)  {

    fun convertToPluginResponse(plugin: Plugin): PluginResponse {
        val fieldConfigurations: List<FieldConfiguration> = objectMapper.readValue(plugin.configurationDescriptors!!)
        return PluginResponse(
            plugin.id!!,
            plugin.name!!,
            plugin.appCode,
            convertToConfigurationDescriptor(fieldConfigurations)
        )
    }

    private fun convertToConfigurationDescriptor(fieldConfigurations: List<FieldConfiguration>): List<ConfigurationDescriptor>{
        return fieldConfigurations.map {fieldConfiguration ->
            ConfigurationDescriptor(
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
}