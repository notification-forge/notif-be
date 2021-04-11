package com.forge.messageservice.resolvers.mutations

import com.forge.messageservice.entity.Template
import com.forge.messageservice.entity.TemplateVersion
import com.forge.messageservice.entity.inputs.CreateTemplateInput
import com.forge.messageservice.entity.inputs.CreateTemplateVersionInput
import com.forge.messageservice.entity.inputs.UpdateTemplateInput
import com.forge.messageservice.entity.inputs.UpdateTemplateVersionInput
import com.forge.messageservice.services.TemplateService
import com.forge.messageservice.services.TemplateVersionService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component

@Component
class TemplateMutation(
    private val templateService: TemplateService,
    private val templateVersionService: TemplateVersionService
) : GraphQLMutationResolver {

    fun createTemplate(input: CreateTemplateInput): Template {
        return templateService.createTemplate(input)
    }

    fun updateTemplate(input: UpdateTemplateInput): Template {
        return templateService.updateTemplate(input)
    }

    fun createTemplateVersion(input: CreateTemplateVersionInput): TemplateVersion{
        return templateVersionService.createTemplateVersion(input)
    }

    fun updateTemplateVersion(input: UpdateTemplateVersionInput): TemplateVersion{
        return templateVersionService.updateTemplateVersion(input)
    }
}