package com.forge.messageservice.resolvers.queries

import com.forge.messageservice.entity.Template
import com.forge.messageservice.entity.TemplateVersion
import com.forge.messageservice.services.TemplateVersionService
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.function.Supplier

@Component
class TemplateVersionResolver(
    private val templateVersionService: TemplateVersionService
): GraphQLResolver<Template>{

    val executorService = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors()
    )

    fun templateVersions(template: Template?): CompletableFuture<List<TemplateVersion>>? {
        return CompletableFuture.supplyAsync(
            Supplier {
                templateVersionService.retrieveAllTemplateVersionsByTemplateId(template!!.id!!)
            },
            executorService
        )
    }
}