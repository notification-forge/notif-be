package com.forge.messageservice.resolvers.queries

import com.alphamail.plugin.api.FieldConfiguration
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.forge.messageservice.entities.Plugin
import com.forge.messageservice.entities.Template
import com.forge.messageservice.entities.TemplateVersion
import com.forge.messageservice.entities.inputs.PluginsInput
import com.forge.messageservice.entities.responses.ConfigurationDescriptor
import com.forge.messageservice.entities.responses.PluginResponse
import com.forge.messageservice.resolvers.queries.responses.PluginResponseConverter
import com.forge.messageservice.services.PluginService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class PluginResolver(
    private val pluginService: PluginService,
    private val pluginResponseConverter: PluginResponseConverter
) : GraphQLQueryResolver {

    fun plugins(appCode: String): List<PluginResponse> {
        return pluginService.findAvailablePluginsForApp(appCode).map {plugin ->
            pluginResponseConverter.convertToPluginResponse(plugin)
        }
    }
}