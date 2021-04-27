package com.forge.messageservice.services

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * Handles [Tenant] and [User] creation
 */
@Service
open class RegistrationService {

    /**
     * Given an [Authentication] check if user is a member of a developer group that's not a tenant yet, and
     * create a mapping between the newly created [Tenant] and the [User]
     *
     */
    @Transactional(Transactional.TxType.REQUIRED)
    open fun registerIfNecessary(authentication: Authentication) {

        // read through the user's security groups
        val authorities = authentication.authorities.map {
            it.authority
        }

        println(authorities)
    }

}