package com.forge.messageservice.services

import com.forge.messageservice.entities.Onboarding
import com.forge.messageservice.entities.Tenant
import com.forge.messageservice.entities.User
import com.forge.messageservice.graphql.models.inputs.*
import com.forge.messageservice.exceptions.TenantDoesNotExistException
import com.forge.messageservice.exceptions.TenantExistedException
import com.forge.messageservice.repositories.OnboardingRepository
import com.forge.messageservice.repositories.TenantRepository
import com.forge.messageservice.repositories.UserRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
class OnboardingServiceTest {

    @MockK
    lateinit var tenantRepository: TenantRepository

    @MockK
    lateinit var userRepository: UserRepository

    @MockK
    lateinit var onboardingRepository: OnboardingRepository

    lateinit var onboardingService: OnboardingService

    @BeforeEach
    fun setUp() {
        onboardingService = OnboardingService(tenantRepository, userRepository, onboardingRepository)
    }

    @AfterEach
    fun tearDown() = unmockkAll()

    companion object{
        const val mockAppCode = "FABK"
        const val mockDisplayName = "Facebook"
        const val mockJustification = "Justified"
        const val mockDescription = "Description"
        const val mockPrimaryOwnerName = "Notification Forge User 1"
        const val mockPrimaryOwnerId = "ntfusr1"
        const val mockSecondaryOwnerName = "Notification Forge User 2"
        const val mockSecondaryOwnerId = "ntfusr2"
    }

    private fun mockCreateAppInput(): CreateAppInput {
        return CreateAppInput(
            mockAppCode,
            mockDisplayName,
            mockJustification,
            mockDescription,
            mockPrimaryOwnerName,
            mockPrimaryOwnerId,
            mockSecondaryOwnerName,
            mockSecondaryOwnerId
        )
    }

    private fun mockUpdateAppInput(): UpdateAppInput {
        return UpdateAppInput(
            mockAppCode,
            mockDisplayName,
            mockJustification,
            mockDescription,
            mockPrimaryOwnerName,
            mockPrimaryOwnerId,
            mockSecondaryOwnerName,
            mockSecondaryOwnerId
        )
    }

    private fun mockOnboardUserInput(): OnboardUserInput {
        return OnboardUserInput(
            mockAppCode,
            mockPrimaryOwnerId,
            mockPrimaryOwnerName
        )
    }

    private fun mockApprovingAppInput(): ApprovalAppInput{
        return ApprovalAppInput(
            mockAppCode,
            Tenant.AppStatus.ACTIVE,
            ""
        )
    }

    private fun mockRejectingAppInput(): ApprovalAppInput{
        return ApprovalAppInput(
            mockAppCode,
            Tenant.AppStatus.REJECTED,
            "App have no rights"
        )
    }

    private fun mockTenant(): Tenant{
        return Tenant().apply {
            appCode = mockAppCode
            displayName = mockDisplayName
            justification = mockJustification
            description = mockDescription
            primaryOwnerId = mockPrimaryOwnerId
            primaryOwnerName = mockPrimaryOwnerName
            secondaryOwnerId = mockSecondaryOwnerId
            secondaryOwnerName = mockSecondaryOwnerName
        }
    }

    private fun mockUser(): User {
        return User().apply{
            username = mockPrimaryOwnerId
            name = mockPrimaryOwnerName
            onboardings = listOf(
                mockOnboarding()
            )
        }
    }

    private fun mockOnboarding(): Onboarding {
        return Onboarding().apply {
            username = mockPrimaryOwnerId
            appCode = mockAppCode
        }
    }

    @Test
    fun itShouldReturnTenantWhenOnboardingApp() {
        val createAppInput = mockCreateAppInput()

        every { tenantRepository.findById(createAppInput.appCode) } returns Optional.empty()
        every { userRepository.findById(createAppInput.primaryOwnerId) } returns Optional.empty()
        every { userRepository.findById(createAppInput.secondaryOwnerId) } returns Optional.empty()
        every { userRepository.save(any()) } returns mockUser()
        every { tenantRepository.save(any()) } returns mockTenant()

        val tenant = onboardingService.onboardApp(createAppInput)

        assert(tenant.appCode == createAppInput.appCode)
        assert(tenant.primaryOwnerId == createAppInput.primaryOwnerId)
        assert(tenant.secondaryOwnerId == createAppInput.secondaryOwnerId)
        assert(tenant.status == Tenant.AppStatus.PENDING_OWNER_APPROVAL)
    }

    @Test
    fun itShouldThrowExceptionWhenOnboardingAppWhereAppCodeAlreadyExist() {
        val createAppInput = mockCreateAppInput()
        every { tenantRepository.findById(createAppInput.appCode) } returns Optional.of(mockTenant())
        assertThrows<TenantExistedException> { onboardingService.onboardApp(createAppInput) }
    }

    @Test
    fun itShouldReturnUserWhenOnboardingUser() {
        val onboardUserInput = mockOnboardUserInput()

        every { tenantRepository.findTenant(onboardUserInput.appCode) } returns mockTenant()
        every { userRepository.findById(onboardUserInput.username) } returns Optional.of(mockUser())
        every { userRepository.save(mockUser()) } returns mockUser()
        every { onboardingRepository.save(any()) } returns mockOnboarding()

        val user = onboardingService.onboardUser(onboardUserInput)

        assert(user.username == onboardUserInput.username)
        assert(user.name == onboardUserInput.name)
        assert(user.onboardings!![0].appCode == onboardUserInput.appCode)
    }

    @Test
    fun itShouldThrowExceptionWhenOnboardingUserOntoAnAppThatDoesNotExist() {
        val onboardUserInput = mockOnboardUserInput()
        every { tenantRepository.findTenant(onboardUserInput.appCode) } returns null
        every { userRepository.findById(onboardUserInput.username) } returns Optional.of(mockUser())
        assertThrows<TenantDoesNotExistException> { onboardingService.onboardUser(onboardUserInput) }
    }

    @Test
    fun itShouldReturnTenantWhenUpdatingApp() {
        val updateAppInput = mockUpdateAppInput()

        every { userRepository.findById(updateAppInput.primaryOwnerId) } returns Optional.empty()
        every { userRepository.findById(updateAppInput.secondaryOwnerId) } returns Optional.empty()
        every { tenantRepository.findTenant(updateAppInput.appCode) } returns mockTenant()
        every { userRepository.save(any()) } returns mockUser()
        every { tenantRepository.save(any()) } returns mockTenant()

        val tenant = onboardingService.updateApp(updateAppInput)

        assert(tenant.appCode == updateAppInput.appCode)
    }

    @Test
    fun itShouldReturnApprovedTenantWhenApprovingApp() {
        val approvalAppInput = mockApprovingAppInput()
        val mockTenant = mockTenant()
        mockTenant.status =  Tenant.AppStatus.ACTIVE

        every { tenantRepository.findTenant(approvalAppInput.appCode) } returns mockTenant()
        every { userRepository.save(any()) } returns mockUser()
        every { tenantRepository.save(any()) } returns mockTenant
        every { userRepository.existsById(any()) } returns true

        val tenant = onboardingService.approveOrRejectApp(approvalAppInput)

        assert(tenant.appCode == approvalAppInput.appCode)
        assert(tenant.status == Tenant.AppStatus.ACTIVE)
        assert(tenant.rejectedReason.isNullOrEmpty())
    }

    @Test
    fun itShouldReturnRejectedTenantWhenRejectingApp() {
        val approvalAppInput = mockRejectingAppInput()
        val mockTenant = mockTenant()
        mockTenant.status =  Tenant.AppStatus.REJECTED
        mockTenant.rejectedReason = approvalAppInput.rejectedReason

        every { tenantRepository.findTenant(approvalAppInput.appCode) } returns mockTenant()
        every { userRepository.save(any()) } returns mockUser()
        every { tenantRepository.save(any()) } returns mockTenant

        val tenant = onboardingService.approveOrRejectApp(approvalAppInput)

        assert(tenant.appCode == approvalAppInput.appCode)
        assert(tenant.status == Tenant.AppStatus.REJECTED)
        assert(tenant.rejectedReason == approvalAppInput.rejectedReason)
    }

    @Test
    fun itShouldReturnTenantByAppCode() {
        every { tenantRepository.findTenant(mockAppCode) } returns mockTenant()
        val tenant = onboardingService.getTenantByAppCode(mockAppCode)
        assert(tenant.appCode == mockAppCode)
    }

    @Test
    fun itShouldReturnUserByUsername() {
        every { userRepository.findById(mockPrimaryOwnerId) } returns Optional.of(mockUser())
        val user = onboardingService.getUserByUsername(mockPrimaryOwnerId)
        assert(user.username == mockPrimaryOwnerId)
        assert(user.name == mockPrimaryOwnerName)
    }

    @Test
    fun itShouldReturnOnboardingsByAppCode() {
        every { onboardingRepository.findAllByAppCode(mockAppCode) } returns listOf(mockOnboarding())
        val onboardings = onboardingService.getOnboardingsByAppCode(mockAppCode)
        assert(onboardings[0].appCode == mockAppCode)
    }

    @Test
    fun itShouldReturnOnboardingsByUsername() {
        every { onboardingRepository.findAllByUsername(mockPrimaryOwnerId) } returns listOf(mockOnboarding())
        val onboardings = onboardingService.getOnboardingsByUsername(mockPrimaryOwnerId)
        assert(onboardings[0].username == mockPrimaryOwnerId)
    }

    @Test
    fun itShouldReturnAllAppsOwnedByUser(){
        every { onboardingRepository.findAllByUsername(mockPrimaryOwnerId) } returns listOf(mockOnboarding())
        every { tenantRepository.findTenant(mockAppCode) } returns mockTenant()
        val tenants = onboardingService.getAppsOwnsByUser(mockPrimaryOwnerId)
        assert(tenants.isNotEmpty())
        tenants.map{
            assert(it.appCode == mockAppCode)
        }
    }
}