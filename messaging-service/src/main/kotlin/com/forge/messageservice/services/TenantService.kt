package com.forge.messageservice.services

import com.forge.messageservice.controllers.exceptions.TenantAlreadyExistException
import com.forge.messageservice.entity.Tenant
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
        assert(newTenant.appCode != null)

        if(tenantRepository.findByAppCode(newTenant.appCode!!) != null) {
            throw TenantAlreadyExistException("App code already exist.")
        }



        return newTenant
    }

}