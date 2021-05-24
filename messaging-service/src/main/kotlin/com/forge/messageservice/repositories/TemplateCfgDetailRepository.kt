package com.forge.messageservice.repositories

import com.alphamail.plugin.api.MessageType
import com.forge.messageservice.entities.TemplateCfgDetail
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TemplateCfgDetailRepository : JpaRepository<TemplateCfgDetail, Int> {
    fun findByTemplateCfgName(templateCfgType: MessageType): List<TemplateCfgDetail>
}