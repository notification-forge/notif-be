package com.forge.messageservice.repositories

import com.alphamail.plugin.api.MessageType
import com.forge.messageservice.entities.TemplateCfg
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TemplateCfgRepository : JpaRepository<TemplateCfg, MessageType>