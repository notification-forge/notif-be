package com.forge.messageservice.resolvers.mutations

import com.forge.messageservice.entities.Template
import com.forge.messageservice.entities.TemplateVersion
import com.forge.messageservice.graphql.models.inputs.*
import com.forge.messageservice.services.TemplateService
import com.forge.messageservice.services.TemplateVersionService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component
import javax.validation.Valid

@Component
class TemplateMutation(
    private val templateService: TemplateService,
    private val templateVersionService: TemplateVersionService
) : GraphQLMutationResolver {

    fun createTemplate(@Valid input: CreateTemplateInput): Template {
        return templateService.createTemplate(input)
    }

    fun updateTemplate(@Valid input: UpdateTemplateInput): Template {
        return templateService.updateTemplate(input)
    }

    fun createTemplateVersion(@Valid input: CreateTemplateVersionInput): TemplateVersion{
        return templateVersionService.createTemplateVersion(input)
    }

    fun cloneTemplateVersion(@Valid input: CloneTemplateVersionInput): TemplateVersion{
        return templateVersionService.cloneTemplateVersion(input)
    }

    fun updateTemplateVersion(@Valid input: UpdateTemplateVersionInput): TemplateVersion{
        return templateVersionService.updateTemplateVersion(input)
    }
}