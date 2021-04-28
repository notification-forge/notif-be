package com.forge.messageservice.services

import com.forge.messageservice.configurations.security.SecurityConfigHolder
import com.forge.messageservice.entities.Tenant
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * Handles [Tenant] and [User] creation
 */
@Service
open class RegistrationService(
    private val tenantService: TenantService,
    private val securityConfigHolder: SecurityConfigHolder
) {

    /**
     * Given an [Authentication] check if user is a member of a developer group that's not a tenant yet, and
     * create a mapping between the newly created [Tenant] and the [User]
     *
     */
    @Transactional(Transactional.TxType.REQUIRED)
    open fun registerMissingApps(authentication: Authentication): List<Tenant> {
        val groupRegex = Regex(securityConfigHolder.groupPattern)

        val apps = authentication.authorities
            .asSequence()
            .map { it.authority.replace(Regex("^ROLE_"), "") }
            .mapNotNull { auth -> groupRegex.find(auth)?.groups }
            .map { groups ->
                val appCode = groups["appcode"]!!.value
                val module = groups.get("module")?.value
                Tenant().apply {
                    this.appCode = appCode
                    this.module = module
                    displayName = appCode
                    status = Tenant.AppStatus.ACTIVE
                    encryptionKey = "TODO"
                }
            }
            .toList()

        apps.forEach { tenant: Tenant -> tenantService.register(tenant) }
        return apps
    }
}