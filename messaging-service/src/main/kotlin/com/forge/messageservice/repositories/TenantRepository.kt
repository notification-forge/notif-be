package com.forge.messageservice.repositories

import com.forge.messageservice.entities.Tenant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TenantRepository : JpaRepository<Tenant, String> {

    @Query("select t from Tenant t where t.appCode = :appCode")
    fun findTenant(@Param("appCode") appCode: String): Tenant?

    @Query("select t from Tenant t where t.appCode = :appCode and t.module = :module")
    fun findTenant(@Param("appCode") appCode: String, @Param("module") module: String?): Tenant?

}