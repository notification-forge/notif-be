package com.forge.messageservice.resolvers.queries

import com.forge.messageservice.entities.Template
import com.forge.messageservice.entities.TemplateVersion
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
    private val templateVersionService: TemplateVersionService
) : GraphQLQueryResolver {

    fun template(templateId: Long): Template {
        return templateService.getTemplateById(templateId)
    }

    fun templateVersion(templateVersionId: Long): TemplateVersion {
        return templateVersionService.getTemplateVersionById(templateVersionId)
    }

    fun templatePages(name: String, appCodes: List<String>, pageRequestInput: PaginationInput): Connection<Template> {
        return gqlConnectionFor({
            templateService.getAllTemplatesWithTemplateNameAndInAppCodes(
                appCodes, name, pageRequestInput.asPageRequest()
            )
        }) {
            DefaultEdge(it, CursorResolver.from(it.id!!))
        }
    }
}