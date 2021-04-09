package com.forge.messageservice.repositories

import com.forge.messageservice.entity.TemplateVersion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TemplateVersionRepository : JpaRepository<TemplateVersion, Long> {}