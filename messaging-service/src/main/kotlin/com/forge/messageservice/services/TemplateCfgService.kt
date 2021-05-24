package com.forge.messageservice.services

import com.alphamail.plugin.api.MessageType
import com.forge.messageservice.entities.TemplateCfg
import com.forge.messageservice.entities.TemplateCfgDetail
import com.forge.messageservice.repositories.TemplateCfgDetailRepository
import com.forge.messageservice.repositories.TemplateCfgRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TemplateCfgService(
    private val templateCfgRepository: TemplateCfgRepository,
    private val templateCfgDetailRepository: TemplateCfgDetailRepository
) {

    @Transactional(readOnly = true)
    fun getCfgFor(cfgType: MessageType): TemplateCfg =
        templateCfgRepository.findById(cfgType).orElseGet { null }

    @Transactional(readOnly = true)
    fun findCfgDetailsFor(cfgType: MessageType): List<TemplateCfgDetail> =
        templateCfgDetailRepository.findByTemplateCfgName(cfgType)
}