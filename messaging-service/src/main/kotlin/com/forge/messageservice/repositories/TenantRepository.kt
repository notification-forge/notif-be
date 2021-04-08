package com.forge.messageservice.repositories

import com.forge.messageservice.entity.Tenant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TenantRepository : JpaRepository<Tenant, Int> {

    fun findByAppCode(appCode: String): Tenant?

}