package com.forge.messageservice.services

import com.forge.messageservice.entity.Template
import com.forge.messageservice.repositories.TemplateRepository
import org.springframework.stereotype.Service

@Service
open class TemplateService(private val templateRepository: TemplateRepository) {

    fun saveTemplate(template: Template): Template {
        return templateRepository.save(template)
    }

    fun retrieveTemplateById(templateId: Long): Template {
        return templateRepository.getOne(templateId)
    }

    fun updateTemplate(otherTemplate: Template, templateId: Long): Template {
        val template = retrieveTemplateById(templateId)
        update(otherTemplate, template)
        return saveTemplate(template)
    }

    fun update(otherTemplate: Template, template: Template) {
        template.apply {
            templateName = otherTemplate.templateName
            appCode = otherTemplate.appCode
            alertType = otherTemplate.alertType
        }
    }
}