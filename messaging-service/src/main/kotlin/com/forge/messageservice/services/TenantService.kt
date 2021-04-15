package com.forge.messageservice.services

import com.forge.messageservice.entities.Tenant
import com.forge.messageservice.exceptions.TenantAlreadyExistException
import com.forge.messageservice.repositories.TenantRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
open class TenantService(private val tenantRepository: TenantRepository) {

    /**
     * Creates a new `Tenant` entry pending for either the primary or secondary app owner to approve.
     *
     * Returns a tenant with the request id
     */
    @Transactional(Transactional.TxType.REQUIRED)
    open fun register(newTenant: Tenant): Tenant {

        if(tenantRepository.findByAppCode(newTenant.appCode) != null) {
            throw TenantAlreadyExistException("App code already exist.")
        }

        return newTenant
    }

    @Transactional(Transactional.TxType.NEVER)
    open fun findTenant(appCode: String): Tenant? {
        return tenantRepository.findByAppCode(appCode)
    }

}