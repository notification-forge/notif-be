package com.forge.messageservice.resolvers.queries

import com.forge.messageservice.entities.Tenant
import com.forge.messageservice.entities.User
import com.forge.messageservice.services.OnboardingService
import com.forge.messageservice.services.TenantService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
class OnboardingResolver(
    private val tenantService: TenantService,
    private val onboardingService: OnboardingService
) : GraphQLQueryResolver {

    fun app(appCode: String): Tenant? {
        return tenantService.findTenant(appCode)
    }

    fun user(username: String): User {
        return onboardingService.getUserByUsername(username)
    }
}