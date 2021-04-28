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
            .mapNotNull { auth -> groupRegex.find(auth)?.groupValues }
            .map { it[2] } // our pattern will capture group2 (0 is entire string, 1 is DCIF, 2 is app name, 3 is user role
            .map { it.split("_") }
            .map { app ->
                Tenant().apply {
                    appCode = app[0]
                    module = if (app.size == 1) null else app[1]
                    displayName = app[0]
                    status = Tenant.AppStatus.ACTIVE
                    encryptionKey = "TODO"
                }
            }
            .toList()

        apps.forEach { tenant: Tenant -> tenantService.register(tenant) }
        return apps
    }
}