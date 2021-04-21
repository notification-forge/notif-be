package com.forge.messageservice.resolvers.mutations

import com.forge.messageservice.entities.Tenant
import com.forge.messageservice.entities.User
import com.forge.messageservice.entities.inputs.ApprovalAppInput
import com.forge.messageservice.entities.inputs.CreateAppInput
import com.forge.messageservice.entities.inputs.OnboardUserInput
import com.forge.messageservice.entities.inputs.UpdateAppInput
import com.forge.messageservice.services.OnboardingService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component
import javax.validation.Valid

@Component
class OnboardingMutation(
    private val onboardingService: OnboardingService
) : GraphQLMutationResolver {

    fun onboardApp(@Valid input: CreateAppInput): Tenant {
        return onboardingService.onboardApp(input)
    }

    fun updateApp(@Valid input: UpdateAppInput): Tenant {
        return onboardingService.updateApp(input)
    }

    fun approveOrRejectAppInput(@Valid input: ApprovalAppInput): Tenant {
        return onboardingService.approveOrRejectApp(input)
    }

    fun onboardUser(@Valid input: OnboardUserInput): User {
        return onboardingService.onboardUser(input)
    }
}