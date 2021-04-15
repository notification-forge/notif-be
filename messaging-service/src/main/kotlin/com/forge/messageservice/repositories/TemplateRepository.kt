package com.forge.messageservice.repositories

import com.forge.messageservice.entities.Template
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TemplateRepository : JpaRepository<Template, Long> {
    fun findByNameAndAppCode(name: String, appCode: String): Template?
    @Query("SELECT t FROM Template t WHERE t.name LIKE '%:name%' AND t.appCode in :appCodes")
    fun findAllLikeNameAndInAppCodes(name: String, appCodes: List<String>): List<Template>
    @Query("SELECT t FROM Template t WHERE t.appCode in :appCodes AND t.id > :id")
    fun findAllInAppCodesAfterTemplateId(appCodes: List<String>, id: Long): List<Template>
}