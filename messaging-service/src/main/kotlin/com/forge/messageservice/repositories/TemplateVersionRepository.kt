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
    @Query("SELECT MAX(t.version) FROM TemplateVersion t where t.templateId = :templateId")
    fun findCurrentVersionNumberByTemplateId(templateId: Long): Long
    fun existsByTemplateIdAndTemplateHash(templateId: Long, templateHash: Int): Boolean
}