package com.forge.messageservice.repositories

import com.forge.messageservice.entities.Template
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TemplateRepository : JpaRepository<Template, Long> {
    fun findByNameAndAppCode(name: String, appCode: String): Template?

    @Query("SELECT t FROM Template t WHERE t.name LIKE %:name% AND t.appCode in :appCodes")
    fun findWithNamesLike(appCodes: List<String>, name: String, pageable: Pageable): Page<Template>
}