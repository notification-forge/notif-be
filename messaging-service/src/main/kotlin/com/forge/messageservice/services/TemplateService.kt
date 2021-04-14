package com.forge.messageservice.services

import com.forge.messageservice.entity.Template
import com.forge.messageservice.entity.inputs.CreateTemplateInput
import com.forge.messageservice.entity.inputs.UpdateTemplateInput
import com.forge.messageservice.exceptions.TemplateDoesNotExistException
import com.forge.messageservice.exceptions.TemplateExistedException
import com.forge.messageservice.repositories.TemplateRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
open class TemplateService(private val templateRepository: TemplateRepository) {

    fun retrieveAllTemplates(): List<Template> {
        return templateRepository.findAll()
    }

    fun retrieveTemplateById(templateId: Long): Template {
        val optionalTemplate = templateRepository.findById(templateId)

        if (optionalTemplate.isEmpty){
            throw TemplateDoesNotExistException("Template with template id $templateId does not exist")
        }
        return optionalTemplate.get()
    }

    private fun retrieveTemplateByTemplateNameAndAppCode(templateName: String, appCode: String): Template? {
        return templateRepository.findByTemplateNameAndAppCode(templateName, appCode)
    }

    fun createTemplate(templateInput: CreateTemplateInput): Template{
        ensureNoTemplateWithSameNameAndAppCodeExist(templateInput.templateName, templateInput.appCode)

        return saveTemplate(Template().apply {
            templateUUID = UUID.randomUUID()
            templateName = templateInput.templateName
            alertType = templateInput.alertType
            appCode = templateInput.appCode
        })
    }

    fun saveTemplate(template: Template): Template {
        return templateRepository.save(template)
    }

    fun updateTemplate(templateInput: UpdateTemplateInput): Template {
        val template = retrieveTemplateById(templateInput.templateId)

        ensureNoTemplateWithSameNameAndAppCodeExist(templateInput.templateName, template.appCode!!)

        update(templateInput, template)
        return saveTemplate(template)
    }

    fun update(templateInput: UpdateTemplateInput, template: Template) {
        template.apply {
            templateName = templateInput.templateName
        }
    }

    private fun ensureNoTemplateWithSameNameAndAppCodeExist(templateName: String, appCode: String){
        val existingTemplate = retrieveTemplateByTemplateNameAndAppCode(templateName, appCode)

        if (existingTemplate != null){
            throw TemplateExistedException("Template with template name $templateName already exist in $appCode")
        }
    }
}