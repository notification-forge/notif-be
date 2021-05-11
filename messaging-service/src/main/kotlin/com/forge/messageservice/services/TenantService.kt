package com.forge.messageservice.services

import com.forge.messageservice.entities.Tenant
import com.forge.messageservice.repositories.TenantRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class TenantService(private val tenantRepository: TenantRepository) {

    /**
     * Register's a new tenant. Does nothing if tenant, tenant/module pair already exist.
     *
     * Returns the newly created tenant (if newly created) or the parameter `newTenant` if it
     * already exists.
     *
     * This function may throw a unique constraint violation error if there happen to be a concurrent write
     * for the same appcode / module pair.
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    fun register(newTenant: Tenant): Tenant {

        if (tenantRepository.findTenant(newTenant.appCode) != null) {
            // Do nothing if tenant already exist.
            return newTenant
        }

        return tenantRepository.saveAndFlush(newTenant)
    }

    @Transactional(Transactional.TxType.NEVER)
    fun findTenant(appCode: String): Tenant? {
        return tenantRepository.findTenant(appCode)
    }

}