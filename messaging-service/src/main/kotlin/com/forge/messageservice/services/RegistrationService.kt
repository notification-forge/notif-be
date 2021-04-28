package com.forge.messageservice.services

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * Handles [Tenant] and [User] creation
 */
@Service
open class RegistrationService {

    private val userPermissionPattern =
        "^ROLE_([A-Z0-1]{7})_([A-I]{4}_)?(?<appCode>[A-Z0-9]+)_((?<module>[A-Z0-9]+)_)?(DEVELOPER|APPLEAD)$".toRegex()

    /**
     * Given an [Authentication] check if user is a member of a developer group that's not a tenant yet, and
     * create a mapping between the newly created [Tenant] and the [User]
     *
     */
    @Transactional(Transactional.TxType.REQUIRED)
    open fun registerIfNecessary(authentication: Authentication) {

        // read through the user's security groups
        val authorities = authentication.authorities
            .mapNotNull { f ->
                val matchResult = userPermissionPattern.find(f.authority)
                when {
                    matchResult != null -> Pair(
                        matchResult.groups["appCode"]?.value, matchResult.groups["module"]?.value)
                    else -> null
                }
            }

        println(authorities)
    }

}