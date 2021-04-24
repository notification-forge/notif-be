package com.forge.messageservice.services

import com.forge.messageservice.entities.Template
import com.forge.messageservice.graphql.models.inputs.CreateTemplateInput
import com.forge.messageservice.graphql.models.inputs.PaginationInput
import com.forge.messageservice.graphql.models.inputs.UpdateTemplateInput
import com.forge.messageservice.exceptions.TemplateDoesNotExistException
import com.forge.messageservice.exceptions.TemplateExistedException
import com.forge.messageservice.repositories.TemplateRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
open class TemplateService(
    private val templateRepository: TemplateRepository
) {

    fun getAllTemplatesWithTemplateNameAndInAppCodes(
        appCodes: List<String>,
        name: String,
        pageable: Pageable
    ): Page<Template> {
        return templateRepository.findWithNamesLike(appCodes, name, pageable)
    }

    fun getTemplateById(templateId: Long): Template {
        val optionalTemplate = templateRepository.findById(templateId)

        if (optionalTemplate.isEmpty) {
            throw TemplateDoesNotExistException("Template with template id $templateId does not exist")
        }
        return optionalTemplate.get()
    }

    private fun findTemplateByTemplateNameAndAppCode(templateName: String, appCode: String): Template? {
        return templateRepository.findByNameAndAppCode(templateName, appCode)
    }

    fun createTemplate(templateInput: CreateTemplateInput): Template {
        ensureNoTemplateWithSameNameAndAppCodeExist(templateInput.name, templateInput.appCode)

        return saveTemplate(Template().apply {
            uuid = UUID.randomUUID()
            name = templateInput.name
            alertType = templateInput.alertType
            appCode = templateInput.appCode
        })
    }

    fun saveTemplate(template: Template): Template {
        return templateRepository.save(template)
    }

    fun updateTemplate(templateInput: UpdateTemplateInput): Template {
        val template = getTemplateById(templateInput.id)

        ensureNoTemplateWithSameNameAndAppCodeExist(templateInput.name, template.appCode!!)

        update(templateInput, template)
        return saveTemplate(template)
    }

    private fun update(templateInput: UpdateTemplateInput, template: Template) {
        template.apply {
            name = templateInput.name
        }
    }

    private fun ensureNoTemplateWithSameNameAndAppCodeExist(templateName: String, appCode: String) {
        val existingTemplate = findTemplateByTemplateNameAndAppCode(templateName, appCode)

        if (existingTemplate != null) {
            throw TemplateExistedException("Template with template name $templateName already exist in $appCode")
        }
    }
}