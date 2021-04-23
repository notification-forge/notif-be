package com.forge.messageservice.repositories

import com.forge.messageservice.entities.TemplatePlugin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TemplatePluginRepository : JpaRepository<TemplatePlugin, Long> {
    fun findAllByTemplateVersionId(templateVersionId: Long): List<TemplatePlugin>
}