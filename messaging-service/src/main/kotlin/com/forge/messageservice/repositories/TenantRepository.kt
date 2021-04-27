package com.forge.messageservice.repositories

import com.forge.messageservice.entities.Tenant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TenantRepository : JpaRepository<Tenant, String> {

    fun findByAppCode(appCode: String): Tenant?
    fun findByAppCodeAndModule(appCode: String, module: String?): Tenant?

}