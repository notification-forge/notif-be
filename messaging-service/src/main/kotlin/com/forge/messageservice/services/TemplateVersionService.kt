package com.forge.messageservice.services

import com.forge.messageservice.entity.TemplateVersion
import com.forge.messageservice.entity.TemplateVersion.TemplateStatus
import com.forge.messageservice.entity.TemplateVersion.TemplateStatus.*
import com.forge.messageservice.entity.inputs.CloneTemplateVersionInput
import com.forge.messageservice.entity.inputs.CreateTemplateVersionInput
import com.forge.messageservice.entity.inputs.UpdateTemplateVersionInput
import com.forge.messageservice.exceptions.TemplateVersionDoesNotExistException
import com.forge.messageservice.repositories.TemplateRepository
import com.forge.messageservice.repositories.TemplateVersionRepository
import org.springframework.stereotype.Service

@Service
open class TemplateVersionService(
    private val templateVersionRepository: TemplateVersionRepository,
    private val templateRepository: TemplateRepository
) {

    fun retrieveAllTemplateVersionsByTemplateId(templateVersionId: Long): List<TemplateVersion> {
        return templateVersionRepository.findAllByTemplateId(templateVersionId)
    }

    fun retrieveTemplateVersionById(templateVersionId: Long): TemplateVersion {
        val optionalTemplateVersion = templateVersionRepository.findById(templateVersionId)

        if (optionalTemplateVersion.isEmpty){
            throw TemplateVersionDoesNotExistException("TemplateVersion with template id $templateVersionId does not exist")
        }
        return optionalTemplateVersion.get()
    }

    private fun retrieveTemplateVersionByTemplateIdAndStatus(templateVersionId: Long, status: TemplateStatus): TemplateVersion? {
        return templateVersionRepository.findByTemplateIdAndStatus(templateVersionId, status)
    }

    fun createTemplateVersion(templateVersionInput: CreateTemplateVersionInput): TemplateVersion {
        val templateVersion = retrieveTemplateVersionByTemplateIdAndStatus(templateVersionInput.templateId, DRAFT)

        ensureTemplateExist(templateVersionInput.templateId)

        if (templateVersion != null){
            return saveTemplateVersion(TemplateVersion().apply {
                id = templateVersion.templateId
                templateId = templateVersionInput.templateId
                status = DRAFT
            })
        }

        return saveTemplateVersion(TemplateVersion().apply {
            templateId = templateVersionInput.templateId
        })
    }

    fun cloneTemplateVersion(templateVersionInput: CloneTemplateVersionInput): TemplateVersion {
        val currentTemplateVersion = retrieveTemplateVersionByTemplateIdAndStatus(templateVersionInput.id, DRAFT)

        ensureTemplateExist(templateVersionInput.id)

        val newTemplateVersion = TemplateVersion().apply {
            templateId = templateVersionInput.id
        }

        if (currentTemplateVersion != null){
            newTemplateVersion.id = currentTemplateVersion.id
        }

        newTemplateVersion.apply {
            name = newTemplateVersion.name
            settings = newTemplateVersion.settings
            body = templateVersionInput.body
            version = templateVersionRepository.findCurrentVersionNumberByTemplateId(newTemplateVersion.templateId!!) + 1
            status = DRAFT
        }

        return saveTemplateVersion(newTemplateVersion)
    }

    private fun saveTemplateVersion(templateVersion: TemplateVersion): TemplateVersion {
        return templateVersionRepository.save(templateVersion)
    }

    fun updateTemplateVersion(templateVersionInput: UpdateTemplateVersionInput): TemplateVersion {
        val templateVersion = retrieveTemplateVersionById(templateVersionInput.id)

        templateVersion.apply {
            name = templateVersionInput.name
            settings = templateVersionInput.settings
            body = templateVersionInput.body
            version = templateVersionRepository.findCurrentVersionNumberByTemplateId(templateVersion.templateId!!) + 1
            status = templateVersionInput.status
        }

        return saveTemplateVersion(templateVersion)
    }

    private fun ensureTemplateExist(templateId: Long) {
        val optionalTemplate = templateRepository.findById(templateId)
        if (optionalTemplate.isEmpty){
            throw TemplateVersionDoesNotExistException("Template with template Id $templateId does not exist")
        }
    }

}