package com.forge.messageservice.repositories

import com.forge.messageservice.entities.TenantUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TenantUserRepo: JpaRepository<TenantUser, Long> {
    fun findByUsername(username: String): List<TenantUser>
    fun findByAppCode(appCode: String): Set<TenantUser>
}