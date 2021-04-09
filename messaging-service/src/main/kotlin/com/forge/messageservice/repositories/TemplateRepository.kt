package com.forge.messageservice.repositories

import com.forge.messageservice.entity.Template
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TemplateRepository : JpaRepository<Template, Long> {}