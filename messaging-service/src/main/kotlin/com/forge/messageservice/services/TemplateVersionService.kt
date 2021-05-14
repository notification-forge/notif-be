package com.forge.messageservice.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.forge.messageservice.entities.MailSettings
import com.forge.messageservice.entities.Template
import com.forge.messageservice.entities.Template.AlertType.EMAIL
import com.forge.messageservice.entities.Template.AlertType.TEAMS
import com.forge.messageservice.entities.TemplateVersion
import com.forge.messageservice.entities.TemplateVersion.TemplateStatus
import com.forge.messageservice.entities.TemplateVersion.TemplateStatus.DRAFT
import com.forge.messageservice.exceptions.TemplateHashExistedException
import com.forge.messageservice.exceptions.TemplateVersionDoesNotExistException
import com.forge.messageservice.graphql.models.inputs.CloneTemplateVersionInput
import com.forge.messageservice.graphql.models.inputs.CreateTemplateVersionInput
import com.forge.messageservice.graphql.models.inputs.UpdateTemplateVersionInput
import com.forge.messageservice.repositories.TemplateRepository
import com.forge.messageservice.repositories.TemplateVersionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class TemplateVersionService(
    private val templatePluginService: TemplatePluginService,
    private val templateVersionRepository: TemplateVersionRepository,
    private val templateRepository: TemplateRepository,
    private val objectMapper: ObjectMapper
) {

    @Transactional(readOnly = true)
    fun getAllTemplateVersionsByTemplateId(templateId: Long): List<TemplateVersion> {
        return templateVersionRepository.findAllByTemplateId(templateId)
    }

    @Transactional(readOnly = true)
    fun findTemplateVersionsByTemplateHashAndTemplateId(templateHash: Int, templateId: Long): TemplateVersion? {
        return templateVersionRepository.findByTemplateHashAndTemplateId(templateHash, templateId)
    }

    @Transactional(readOnly = true)
    fun getTemplateVersionById(templateVersionId: Long): TemplateVersion {
        val optionalTemplateVersion = templateVersionRepository.findById(templateVersionId)

        if (optionalTemplateVersion.isEmpty) {
            throw TemplateVersionDoesNotExistException("TemplateVersion with template id $templateVersionId does not exist")
        }
        return optionalTemplateVersion.get()
    }

    @Transactional(readOnly = true)
    private fun findTemplateVersionByTemplateIdAndStatus(
        templateVersionId: Long,
        status: TemplateStatus
    ): TemplateVersion? {
        return templateVersionRepository.findByTemplateIdAndStatus(templateVersionId, status)
    }

    @Transactional(propagation = Propagation.REQUIRED)
    fun createTemplateVersion(templateVersionInput: CreateTemplateVersionInput): TemplateVersion {
        val templateVersion = findTemplateVersionByTemplateIdAndStatus(templateVersionInput.templateId, DRAFT)

        val template = getTemplateExist(templateVersionInput.templateId)

        val newTemplateVersion = TemplateVersion().apply {
            templateId = templateVersionInput.templateId
            status = DRAFT
            templateHash = 0
            settings = getTemplateSetting(template)
        }

        if (templateVersion != null) {
            return newTemplateVersion.apply {
                id = templateVersion.templateId
            }
        }

        return saveTemplateVersion(newTemplateVersion)
    }

    @Transactional(propagation = Propagation.REQUIRED)
    fun cloneTemplateVersion(templateVersionInput: CloneTemplateVersionInput): TemplateVersion {
        val currentTemplateVersion = findTemplateVersionByTemplateIdAndStatus(templateVersionInput.templateId, DRAFT)

        getTemplateExist(templateVersionInput.templateId)

        val newTemplateVersion = TemplateVersion().apply {
            templateId = templateVersionInput.templateId
        }

        if (currentTemplateVersion != null) {
            newTemplateVersion.id = currentTemplateVersion.id
        }

        newTemplateVersion.apply {
            name = newTemplateVersion.name
            settings = newTemplateVersion.settings
            body = templateVersionInput.body
            version =
                templateVersionRepository.findCurrentVersionNumberByTemplateId(newTemplateVersion.templateId!!) + 1
            status = DRAFT
        }
        newTemplateVersion.templateHash = newTemplateVersion.templateHash()

        val savedTemplateVersion = saveTemplateVersion(newTemplateVersion)

        templatePluginService.createTemplatePlugins(
            savedTemplateVersion.templateId!!,
            templateVersionInput.plugins
        )

        return savedTemplateVersion
    }

    private fun saveTemplateVersion(templateVersion: TemplateVersion): TemplateVersion {
//        ensureTemplateHashDoesNotExist(templateVersion)
        return templateVersionRepository.save(templateVersion)
    }

    @Transactional(propagation = Propagation.REQUIRED)
    fun updateTemplateVersion(templateVersionInput: UpdateTemplateVersionInput): TemplateVersion {
        val templateVersion = getTemplateVersionById(templateVersionInput.id)

        templateVersion.apply {
            name = templateVersionInput.name
            settings = templateVersionInput.settings
            body = templateVersionInput.body
            version = templateVersionRepository.findCurrentVersionNumberByTemplateId(templateVersion.templateId!!) + 1
            status = templateVersionInput.status
        }
        templateVersion.templateHash = templateVersion.templateHash()

        val savedTemplateVersion = saveTemplateVersion(templateVersion)

        templatePluginService.createTemplatePlugins(
            savedTemplateVersion.id!!,
            templateVersionInput.plugins
        )

        return savedTemplateVersion
    }

    private fun getTemplateSetting(template: Template, ): String {
        return when (template.alertType) {
            EMAIL -> {
                objectMapper.writeValueAsString(MailSettings())
            }
            TEAMS -> {
                ""
            }
        }
    }

    private fun getTemplateExist(templateId: Long): Template {
        val optionalTemplate = templateRepository.findById(templateId)
        if (optionalTemplate.isEmpty) {
            throw TemplateVersionDoesNotExistException("Template with template Id $templateId does not exist")
        }
        return optionalTemplate.get()
    }

    private fun ensureTemplateHashDoesNotExist(templateVersion: TemplateVersion) {
        if (templateVersionRepository.existsByTemplateIdAndTemplateHash(
                templateVersion.templateId!!,
                templateVersion.templateHash!!
            )
        ) {
            throw TemplateHashExistedException("Template with the exact same body and setting has already exist")
        }
    }
}