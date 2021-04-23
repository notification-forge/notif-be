package com.forge.messageservice.resolvers.queries

import com.forge.messageservice.entities.Onboarding
import com.forge.messageservice.entities.Plugin
import com.forge.messageservice.entities.TemplateVersion
import com.forge.messageservice.entities.Tenant
import com.forge.messageservice.entities.responses.PluginResponse
import com.forge.messageservice.resolvers.queries.responses.PluginResponseConverter
import com.forge.messageservice.services.OnboardingService
import com.forge.messageservice.services.PluginService
import com.forge.messageservice.services.TemplatePluginService
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.function.Supplier

@Component
class TemplateVersionPluginsResolver(
    private val pluginService: PluginService,
    private val templatePluginService: TemplatePluginService,
    private val pluginResponseConverter: PluginResponseConverter
) : GraphQLResolver<TemplateVersion> {

    private val executorService = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors()
    )

    fun plugins(templateVersion: TemplateVersion?): CompletableFuture<List<PluginResponse>>? {
        return CompletableFuture.supplyAsync(
            Supplier {
                templatePluginService.getTemplatePluginsByTemplateId(templateVersion!!.id!!).map {templatePlugin ->
                    val plugin = pluginService.getPlugin(templatePlugin.pluginId!!)
                    pluginResponseConverter.convertToPluginResponse(plugin)
                }

            },
            executorService
        )
    }
}