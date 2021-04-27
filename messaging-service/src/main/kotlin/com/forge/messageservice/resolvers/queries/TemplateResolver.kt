package com.forge.messageservice.resolvers.queries

import com.forge.messageservice.entities.Template
import com.forge.messageservice.entities.TemplateVersion
import com.forge.messageservice.graphql.CursorResolver
import com.forge.messageservice.graphql.GraphQLConnection
import com.forge.messageservice.graphql.extensions.Connection
import com.forge.messageservice.graphql.models.inputs.PaginationInput
import com.forge.messageservice.services.TemplateService
import com.forge.messageservice.services.TemplateVersionService
import graphql.kickstart.tools.GraphQLQueryResolver

import graphql.relay.DefaultEdge
import graphql.relay.DefaultPageInfo
import org.springframework.stereotype.Component

@Component
class TemplateResolver(
    private val templateService: TemplateService,
    private val templateVersionService: TemplateVersionService
) : GraphQLQueryResolver {

    fun template(templateId: Long): Template {
        return templateService.getTemplateById(templateId)
    }

    fun templateVersion(templateVersionId: Long): TemplateVersion {
        return templateVersionService.getTemplateVersionById(templateVersionId)
    }

    fun templatePages(name: String, appCodes: List<String>, pageRequestInput: PaginationInput): Connection<Template> {
        val templates = templateService.getAllTemplatesWithTemplateNameAndInAppCodes(
            appCodes, name, pageRequestInput.asPageRequest()
        )

        val edges = templates.content.map { template ->
            DefaultEdge(template, CursorResolver.from(template.id!!))
        }

        return GraphQLConnection(
            templates.totalElements.toInt(),
            edges,
            DefaultPageInfo(
                CursorResolver.startCursor(edges),
                CursorResolver.endCursor(edges),
                templates.hasPrevious(),
                templates.hasNext()
            )
        )
    }
}