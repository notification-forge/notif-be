package com.forge.messageservice.services

import com.forge.messageservice.configurations.security.SecurityConfigHolder
import com.forge.messageservice.entities.Tenant
import com.forge.messageservice.entities.TenantUser
import com.forge.messageservice.entities.User
import com.forge.messageservice.repositories.TenantUserRepo
import com.forge.messageservice.repositories.UserRepository
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * Handles [Tenant] and [User] creation
 */
@Service
open class RegistrationService(
    private val tenantService: TenantService,
    private val tenantUserRepo: TenantUserRepo,
    private val securityConfigHolder: SecurityConfigHolder,
    private val userRepository: UserRepository
) {

    open fun registerMissingUser(username: String) : User {
        val userOpt = userRepository.findById(username)
        return if (userOpt.isEmpty) userRepository.save(User().apply { this.username=username }) else userOpt.get()
    }

    /**
     * Given an [Authentication] check if user is a member of a developer group that's not a tenant yet, and
     * create a mapping between the newly created [Tenant] and the [User]
     *
     */
    @Transactional(Transactional.TxType.REQUIRED)
    open fun registerMissingApps(user: User, authentication: Authentication): Set<Tenant> {
        val groupRegex = Regex(securityConfigHolder.groupPattern)

        val apps = authentication.authorities
            .asSequence()
            .map { it.authority.replace(Regex("^ROLE_"), "") }
            .mapNotNull { auth -> groupRegex.find(auth)?.groups }
            .map { groups ->
                val appCode = groups["appcode"]!!.value
                Tenant().apply {
                    this.appCode = appCode
                    displayName = appCode
                    status = Tenant.AppStatus.ACTIVE
                    encryptionKey = "TODO"
                }
            }
            .toSet()
        apps.forEach { tenant -> tenantService.register(tenant) }

        registerMissingAppUser(user, apps)

        return apps
    }

    open fun registerMissingAppUser(user: User, apps: Set<Tenant>) {
        val currentUserApps = user.tenantUsers.map { it.appCode }.toSet()
        apps.filter{ app -> !currentUserApps.contains(app.appCode) }.forEach { app ->
            tenantUserRepo.save(TenantUser().apply {
                appCode = app.appCode
                username = user.username
            })
        }
    }
}