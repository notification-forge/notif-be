package com.forge.messageservice.services

import com.forge.messageservice.authentication.UserContext
import com.forge.messageservice.entities.Onboarding
import com.forge.messageservice.entities.Tenant
import com.forge.messageservice.entities.User
import com.forge.messageservice.entities.inputs.ApprovalAppInput
import com.forge.messageservice.entities.inputs.CreateAppInput
import com.forge.messageservice.entities.inputs.OnboardUserInput
import com.forge.messageservice.entities.inputs.UpdateAppInput
import com.forge.messageservice.exceptions.AppDoesNotExistException
import com.forge.messageservice.exceptions.AppExistedException
import com.forge.messageservice.exceptions.UserHaveYetToOnboardException
import com.forge.messageservice.repositories.OnboardingRepository
import com.forge.messageservice.repositories.TenantRepository
import com.forge.messageservice.repositories.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
open class OnboardingService(
    private val tenantRepository: TenantRepository,
    private val userRepository: UserRepository,
    private val onboardingRepository: OnboardingRepository
) {

    fun getAllApps(): List<Tenant> {
        return tenantRepository.findAll()
    }

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

    fun onboardUser(onboardUserInput: OnboardUserInput): User {
        val user = createOrGetCurrentUser(User().apply {
            username = onboardUserInput.username
            name = onboardUserInput.name
        })
        val app = getAppByAppCode(onboardUserInput.appCode)

        onboardingRepository.save(Onboarding().apply {
            appCode = app.appCode
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

    fun updateApp(appInput: UpdateAppInput): Tenant {
        val app = getAppByAppCode(appInput.appCode)
        update(app, appInput)
        return tenantRepository.save(app)
    }

    fun approveOrRejectApp(appInput: ApprovalAppInput): Tenant {
        val app = getAppByAppCode(appInput.appCode)
        approveOrReject(app, appInput)
        return tenantRepository.save(app)
    }

    fun getAppByAppCode(appCode: String): Tenant {
        return tenantRepository.findByAppCode(appCode)
            ?: throw AppDoesNotExistException("App with app code $appCode does not exist")
    }

    fun getUserByUsername(username: String): User {
        val user = userRepository.findById(username)
        if (user.isEmpty) {
            throw UserHaveYetToOnboardException("User $username has yet to onboard")
        }
        return user.get()
    }

    fun getOnboardingsByAppCode(appCode: String): List<Onboarding> {
        return onboardingRepository.findAllByAppCode(appCode)
    }

    fun getOnboardingsByUsername(username: String): List<Onboarding> {
        return onboardingRepository.findAllByUsername(username)
    }

    fun getAppsOwnsByUser(username: String): List<Tenant> {
        return getOnboardingsByUsername(username).map {
            getAppByAppCode(it.appCode!!)
        }
    }

    private fun update(app: Tenant, appInput: UpdateAppInput) {
        val primaryOwner = createOrGetCurrentUser(User().apply {
            username = appInput.primaryOwnerId
            name = appInput.primaryOwnerName
        })
        val secondaryOwner = createOrGetCurrentUser(User().apply {
            username = appInput.secondaryOwnerId
            name = appInput.secondaryOwnerName
        })

        app.apply {
            displayName = appInput.name
            description = appInput.description
            justification = appInput.justification
            primaryOwnerId = primaryOwner.username
            primaryOwnerName = primaryOwner.name
            secondaryOwnerId = secondaryOwner.username
            secondaryOwnerName = secondaryOwner.name
        }
    }

    private fun approveOrReject(app: Tenant, appInput: ApprovalAppInput) {
        app.apply {
            status = appInput.status
        }
        if (app.status == Tenant.AppStatus.ACTIVE) {
            app.apiToken = "12345"
            app.approvedBy = UserContext.loggedInUsername()
            app.approvedDate = LocalDateTime.now()
        } else if (app.status == Tenant.AppStatus.REJECTED) {
            app.rejectedReason = appInput.rejectedReason
            app.rejectedBy = UserContext.loggedInUsername()
            app.rejectedDate = LocalDateTime.now()
        }
    }

    private fun ensureAppDoesNotExist(appCode: String) {
        val existingApp = tenantRepository.findById(appCode)

        if (existingApp.isPresent) {
            throw AppExistedException("App with app code $appCode already exist")
        }
    }
}