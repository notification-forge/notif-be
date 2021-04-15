package com.forge.messageservice.repositories

import com.forge.messageservice.entities.Tenant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TenantRepository : JpaRepository<Tenant, Long> {

    @Query("select t from Tenant t where lower(t.appCode) = lower(:appCode) and t.status = 'ACTIVE'")
    fun findByAppCode(appCode: String): Tenant?

}