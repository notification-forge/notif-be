package com.forge.messageservice.resolvers.queries

import com.forge.messageservice.entities.responses.PluginResponse
import com.forge.messageservice.resolvers.queries.responses.PluginResponseConverter
import com.forge.messageservice.services.PluginService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
class PluginResolver(
    private val pluginService: PluginService,
    private val pluginResponseConverter: PluginResponseConverter
) : GraphQLQueryResolver {

    fun plugins(appCode: String): List<PluginResponse> {
        return pluginService.findAvailablePluginsForApp(appCode).map { plugin ->
            pluginResponseConverter.convertToPluginResponse(plugin)
        }
    }
}