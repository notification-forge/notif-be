package com.forge.messageservice.resolvers.queries

import com.forge.messageservice.entities.Template
import com.forge.messageservice.entities.TemplateVersion
import com.forge.messageservice.entities.inputs.PaginationInput
import com.forge.messageservice.entities.pages.TemplatePages
import com.forge.messageservice.services.PaginationService
import com.forge.messageservice.services.TemplateService
import com.forge.messageservice.services.TemplateVersionService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
class TemplateResolver(
    private val paginationService: PaginationService,
    private val templateService: TemplateService,
    private val templateVersionService: TemplateVersionService
) : GraphQLQueryResolver {

    fun template(templateId: Long): Template {
        return templateService.getTemplateById(templateId)
    }

    fun templateVersion(templateVersionId: Long): TemplateVersion {
        return templateVersionService.getTemplateVersionById(templateVersionId)
    }

    fun templatePages(name: String, appCodes: List<String>, paginationInput: PaginationInput): TemplatePages {
        val templates = templateService.getAllTemplatesWithTemplateNameAndInAppCodes(appCodes, name, paginationInput)
        return paginationService.templatePagination(templates)
    }
}