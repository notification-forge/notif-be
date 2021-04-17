package com.forge.messageservice.services

import com.forge.messageservice.entities.TemplateVersion
import com.forge.messageservice.entities.TemplateVersion.TemplateStatus
import com.forge.messageservice.entities.TemplateVersion.TemplateStatus.*
import com.forge.messageservice.entities.inputs.CloneTemplateVersionInput
import com.forge.messageservice.entities.inputs.CreateTemplateVersionInput
import com.forge.messageservice.entities.inputs.UpdateTemplateVersionInput
import com.forge.messageservice.exceptions.TemplateVersionDoesNotExistException
import com.forge.messageservice.repositories.TemplateRepository
import com.forge.messageservice.repositories.TemplateVersionRepository
import org.springframework.stereotype.Service

@Service
open class TemplateVersionService(
    private val templateVersionRepository: TemplateVersionRepository,
    private val templateRepository: TemplateRepository
) {

    fun getAllTemplateVersionsByTemplateId(templateId: Long): List<TemplateVersion> {
        return templateVersionRepository.findAllByTemplateId(templateId)
    }

    fun getTemplateVersionById(templateVersionId: Long): TemplateVersion {
        val optionalTemplateVersion = templateVersionRepository.findById(templateVersionId)

        if (optionalTemplateVersion.isEmpty){
            throw TemplateVersionDoesNotExistException("TemplateVersion with template id $templateVersionId does not exist")
        }
        return optionalTemplateVersion.get()
    }

    private fun findTemplateVersionByTemplateIdAndStatus(templateVersionId: Long, status: TemplateStatus): TemplateVersion? {
        return templateVersionRepository.findByTemplateIdAndStatus(templateVersionId, status)
    }

    fun createTemplateVersion(templateVersionInput: CreateTemplateVersionInput): TemplateVersion {
        val templateVersion = findTemplateVersionByTemplateIdAndStatus(templateVersionInput.templateId, DRAFT)

        ensureTemplateExist(templateVersionInput.templateId)

        val newTemplateVersion = TemplateVersion().apply {
            templateId = templateVersionInput.templateId
            status = DRAFT
            templateHash = 0
        }

        if (templateVersion != null){
            return newTemplateVersion.apply {
                id = templateVersion.templateId
            }
        }

        return saveTemplateVersion(newTemplateVersion)
    }

    fun cloneTemplateVersion(templateVersionInput: CloneTemplateVersionInput): TemplateVersion {
        val currentTemplateVersion = findTemplateVersionByTemplateIdAndStatus(templateVersionInput.templateId, DRAFT)

        ensureTemplateExist(templateVersionInput.templateId)

        val newTemplateVersion = TemplateVersion().apply {
            templateId = templateVersionInput.templateId
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
        newTemplateVersion.templateHash = newTemplateVersion.templateHash()

        return saveTemplateVersion(newTemplateVersion)
    }

    private fun saveTemplateVersion(templateVersion: TemplateVersion): TemplateVersion {
        return templateVersionRepository.save(templateVersion)
    }

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

        return saveTemplateVersion(templateVersion)
    }

    private fun ensureTemplateExist(templateId: Long) {
        val optionalTemplate = templateRepository.findById(templateId)
        if (optionalTemplate.isEmpty){
            throw TemplateVersionDoesNotExistException("Template with template Id $templateId does not exist")
        }
    }

}