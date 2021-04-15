package com.forge.messageservice.repositories

import com.forge.messageservice.entities.Tenant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TenantRepository : JpaRepository<Tenant, Int> {

    fun findByAppCode(appCode: String): Tenant?

}