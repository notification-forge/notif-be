package com.forge.messageservice.repositories

import com.forge.messageservice.entities.TemplateVersion
import com.forge.messageservice.entities.TemplateVersion.TemplateStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TemplateVersionRepository : JpaRepository<TemplateVersion, Long> {

    fun findAllByTemplateId(templateId: Long): List<TemplateVersion>

    fun findByTemplateIdAndStatus(templateId: Long, status: TemplateStatus): TemplateVersion?

    fun findByTemplateDigestAndTemplateId(templateDigest: String, templateId: Long): TemplateVersion?

    @Query("SELECT MAX(t.version) FROM TemplateVersion t where t.templateId = :templateId and template_status = 'PUBLISHED'")
    fun findCurrentPublishedVersionNumberByTemplateId(templateId: Long): Long

    @Query("SELECT MAX(t.version) FROM TemplateVersion t where t.templateId = :templateId")
    fun findCurrentVersionNumberByTemplateId(templateId: Long): Long

    fun existsByTemplateIdAndTemplateDigest(templateId: Long, templateDigest: String): Boolean
}