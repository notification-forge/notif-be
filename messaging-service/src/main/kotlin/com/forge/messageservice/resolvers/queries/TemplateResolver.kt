package com.forge.messageservice.resolvers.queries

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.forge.messageservice.common.engines.TemplatingEngine
import com.forge.messageservice.entities.Template
import com.forge.messageservice.entities.TemplateVersion
import com.forge.messageservice.entities.responses.TemplatePreview
import com.forge.messageservice.graphql.CursorResolver
import com.forge.messageservice.graphql.extensions.Connection
import com.forge.messageservice.graphql.models.inputs.PaginationInput
import com.forge.messageservice.resolvers.queries.helpers.GQLConnectionHelper.gqlConnectionFor
import com.forge.messageservice.services.TemplateService
import com.forge.messageservice.services.TemplateVersionService
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.relay.DefaultEdge
import org.springframework.stereotype.Component

@Component
class TemplateResolver(
    private val templateService: TemplateService,
    private val templateVersionService: TemplateVersionService,
    private val templatingEngine: TemplatingEngine,
    private val objectMapper: ObjectMapper
) : GraphQLQueryResolver {

    fun template(templateId: Long): Template {
        return templateService.getTemplateById(templateId)
    }

    fun templateVersion(templateVersionId: Long): TemplateVersion {
        return templateVersionService.getTemplateVersion(templateVersionId)
    }

    fun templates(name: String, appCodes: List<String>, pageRequestInput: PaginationInput): Connection<Template> {
        if (pageRequestInput.sortField.isNullOrEmpty()) {
            pageRequestInput.sortField = "name"
        }
        return gqlConnectionFor({
            templateService.getAllTemplatesWithTemplateNameAndInAppCodes(
                appCodes, name, pageRequestInput.asPageRequest(), pageRequestInput.sortField!!
            )
        }) {
            DefaultEdge(it, CursorResolver.from(it.id!!))
        }
    }

    fun preview(template: String, context: String): TemplatePreview {
        val contextMap: Map<String, String> = objectMapper.readValue(context)
        return TemplatePreview(templatingEngine.parseTemplate(template, contextMap))
    }
}