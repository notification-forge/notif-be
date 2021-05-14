package com.forge.messageservice.services

import com.forge.messageservice.authentication.UserContext
import com.forge.messageservice.entities.Onboarding
import com.forge.messageservice.entities.Tenant
import com.forge.messageservice.entities.User
import com.forge.messageservice.graphql.models.inputs.ApprovalAppInput
import com.forge.messageservice.graphql.models.inputs.CreateAppInput
import com.forge.messageservice.graphql.models.inputs.OnboardUserInput
import com.forge.messageservice.graphql.models.inputs.UpdateAppInput
import com.forge.messageservice.exceptions.TenantDoesNotExistException
import com.forge.messageservice.exceptions.TenantExistedException
import com.forge.messageservice.exceptions.OwnersMissingException
import com.forge.messageservice.exceptions.UserHaveYetToOnboardException
import com.forge.messageservice.repositories.OnboardingRepository
import com.forge.messageservice.repositories.TenantRepository
import com.forge.messageservice.repositories.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class OnboardingService(
    private val tenantRepository: TenantRepository,
    private val userRepository: UserRepository,
    private val onboardingRepository: OnboardingRepository
) {

    @Transactional(propagation = Propagation.REQUIRED)
    fun onboardApp(appInput: CreateAppInput): Tenant {
        ensureAppDoesNotExist(appInput.appCode)

        val primaryOwner = createOrGetCurrentUser(User().apply {
            username = appInput.primaryOwnerId
            name = appInput.primaryOwnerName
        })
        val secondaryOwner = createOrGetCurrentUser(User().apply {
            username = appInput.secondaryOwnerId
            name = appInput.secondaryOwnerName
        })

        return tenantRepository.save(Tenant().apply {
            appCode = appInput.appCode
            displayName = appInput.name
            description = appInput.description
            justification = appInput.justification
            encryptionKey = ""
            primaryOwnerId = primaryOwner.username
            primaryOwnerName = primaryOwner.name
            secondaryOwnerId = secondaryOwner.username
            secondaryOwnerName = secondaryOwner.name
        })
    }

    @Transactional(propagation = Propagation.REQUIRED)
    fun onboardUser(onboardUserInput: OnboardUserInput): User {
        val user = createOrGetCurrentUser(User().apply {
            username = onboardUserInput.username
            name = onboardUserInput.name
        })
        val tenant = getTenantByAppCode(onboardUserInput.appCode)

        onboardingRepository.save(Onboarding().apply {
            appCode = tenant.appCode
            username = user.username
        })
        return user
    }

    private fun createOrGetCurrentUser(user: User): User {
        val optionalUser = userRepository.findById(user.username)
        return if (optionalUser.isEmpty) {
            userRepository.save(user)
        } else {
            optionalUser.get()
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    fun updateApp(appInput: UpdateAppInput): Tenant {
        val tenant = getTenantByAppCode(appInput.appCode)
        update(tenant, appInput)
        return tenantRepository.save(tenant)
    }


    @Transactional(propagation = Propagation.REQUIRED)
    fun approveOrRejectApp(appInput: ApprovalAppInput): Tenant {
        val tenant = getTenantByAppCode(appInput.appCode)
        approveOrReject(tenant, appInput)
        return tenantRepository.save(tenant)
    }

    @Transactional(readOnly = true)
    fun getTenantByAppCode(appCode: String): Tenant {
        return tenantRepository.findTenant(appCode)
            ?: throw TenantDoesNotExistException("App with app code $appCode does not exist")
    }

    @Transactional(readOnly = true)
    fun getUserByUsername(username: String): User {
        val user = userRepository.findById(username)
        if (user.isEmpty) {
            throw UserHaveYetToOnboardException("User $username has yet to onboard")
        }
        return user.get()
    }

    @Transactional(readOnly = true)
    fun getOnboardingsByAppCode(appCode: String): List<Onboarding> {
        return onboardingRepository.findAllByAppCode(appCode)
    }

    @Transactional(readOnly = true)
    fun getOnboardingsByUsername(username: String): List<Onboarding> {
        return onboardingRepository.findAllByUsername(username)
    }

    @Transactional(readOnly = true)
    fun getAppsOwnsByUser(username: String): List<Tenant> {
        return getOnboardingsByUsername(username).map {
            getTenantByAppCode(it.appCode!!)
        }
    }

    private fun update(tenant: Tenant, appInput: UpdateAppInput) {
        val primaryOwner = createOrGetCurrentUser(User().apply {
            username = appInput.primaryOwnerId
            name = appInput.primaryOwnerName
        })
        val secondaryOwner = createOrGetCurrentUser(User().apply {
            username = appInput.secondaryOwnerId
            name = appInput.secondaryOwnerName
        })

        tenant.apply {
            displayName = appInput.name
            description = appInput.description
            justification = appInput.justification
            primaryOwnerId = primaryOwner.username
            primaryOwnerName = primaryOwner.name
            secondaryOwnerId = secondaryOwner.username
            secondaryOwnerName = secondaryOwner.name
        }
    }

    private fun approveOrReject(tenant: Tenant, appInput: ApprovalAppInput) {
        tenant.apply {
            status = appInput.status
        }
        if (tenant.status == Tenant.AppStatus.ACTIVE) {
            ensureOwnersExist(tenant.primaryOwnerId)
            ensureOwnersExist(tenant.secondaryOwnerId)
            tenant.apiToken = "12345"
            tenant.approvedBy = UserContext.loggedInUsername()
            tenant.approvedDate = LocalDateTime.now()
        } else if (tenant.status == Tenant.AppStatus.REJECTED) {
            tenant.rejectedReason = appInput.rejectedReason
            tenant.rejectedBy = UserContext.loggedInUsername()
            tenant.rejectedDate = LocalDateTime.now()
        }
    }

    private fun ensureAppDoesNotExist(appCode: String) {
        val existingApp = tenantRepository.findById(appCode)

        if (existingApp.isPresent) {
            throw TenantExistedException("App with app code $appCode already exist")
        }
    }

    private fun ensureOwnersExist(owner: String?) {
        if (!userExist(owner)) {
            throw OwnersMissingException("Owners cannot be missing when approving application")
        }
    }

    private fun userExist(username: String?): Boolean {
        if (username == null) return false
        return userRepository.existsById(username)
    }
}