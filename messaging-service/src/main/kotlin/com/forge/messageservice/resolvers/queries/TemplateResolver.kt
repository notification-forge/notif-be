package com.forge.messageservice.resolvers.queries

import com.forge.messageservice.entity.Template
import com.forge.messageservice.entity.TemplateVersion
import com.forge.messageservice.services.TemplateService
import com.forge.messageservice.services.TemplateVersionService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component
class TemplateResolver : GraphQLQueryResolver {

    @Resource
    lateinit var templateService: TemplateService
    @Resource
    lateinit var templateVersionService: TemplateVersionService

    fun template(templateId: Long): Template{
        return templateService.retrieveTemplateById(templateId)
    }

    fun templates(): List<Template>{
        return templateService.retrieveAllTemplates()
    }

    fun templateVersion(templateVersionId: Long): TemplateVersion{
        return templateVersionService.retrieveTemplateVersionById(templateVersionId)
    }
}