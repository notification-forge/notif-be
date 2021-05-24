package com.forge.messageservice.services

import com.forge.messageservice.entities.Template
import com.forge.messageservice.exceptions.GraphQLQueryException
import com.forge.messageservice.exceptions.TemplateDoesNotExistException
import com.forge.messageservice.exceptions.TemplateExistedException
import com.forge.messageservice.graphql.models.inputs.CreateTemplateInput
import com.forge.messageservice.graphql.models.inputs.UpdateTemplateInput
import com.forge.messageservice.repositories.TemplateRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TemplateService(
    private val templateRepository: TemplateRepository
) {

    @Transactional(readOnly = true)
    fun getAllTemplatesWithTemplateNameAndInAppCodes(
        appCodes: List<String>,
        name: String,
        pageable: Pageable,
        sortField: String
    ): Page<Template> {
        try {
            return templateRepository.findWithNamesLike(appCodes, name, pageable)
        } catch (e: Exception) {
            throw GraphQLQueryException("sortField: $sortField is invalid")
        }
    }

    @Transactional(readOnly = true)
    fun getTemplateById(templateId: Long): Template {
        val optionalTemplate = templateRepository.findById(templateId)

        if (optionalTemplate.isEmpty) {
            throw TemplateDoesNotExistException("Template with template id $templateId does not exist")
        }
        return optionalTemplate.get()
    }

    @Transactional(readOnly = true)
    fun getTemplateByTemplateNameAndAppCode(templateName: String, appCode: String): Template {
        return findTemplateByTemplateNameAndAppCode(templateName, appCode)
            ?: throw TemplateDoesNotExistException("Template with template name $templateName and app code $appCode does not exist")
    }

    private fun findTemplateByTemplateNameAndAppCode(templateName: String, appCode: String): Template? {
        return templateRepository.findByNameAndAppCode(templateName, appCode)
    }

    @Transactional(readOnly = true)
    fun getTemplateByTemplateUUID(templateUUID: UUID): Template {
        return templateRepository.findByUuid(templateUUID)
            ?: throw TemplateDoesNotExistException("Template with template UUID $templateUUID does not exist")
    }

    @Transactional(propagation = Propagation.REQUIRED)
    fun createTemplate(templateInput: CreateTemplateInput): Template {
        ensureNoTemplateWithSameNameAndAppCodeExist(templateInput.name, templateInput.appCode)

        return saveTemplate(Template().apply {
            uuid = UUID.randomUUID()
            name = templateInput.name
            type = templateInput.alertType
            appCode = templateInput.appCode
        })
    }

    fun saveTemplate(template: Template): Template {
        return templateRepository.save(template)
    }

    @Transactional(propagation = Propagation.REQUIRED)
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