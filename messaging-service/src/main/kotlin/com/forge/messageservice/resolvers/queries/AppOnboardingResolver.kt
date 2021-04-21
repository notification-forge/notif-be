package com.forge.messageservice.resolvers.queries

import com.forge.messageservice.entities.Onboarding
import com.forge.messageservice.entities.Tenant
import com.forge.messageservice.services.OnboardingService
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.function.Supplier

@Component
class AppOnboardingResolver(
    private val onboardingService: OnboardingService
) : GraphQLResolver<Tenant> {

    private val executorService = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors()
    )

    fun onboardings(app: Tenant?): CompletableFuture<List<Onboarding>>? {
        return CompletableFuture.supplyAsync(
            Supplier {
                onboardingService.getOnboardingsByAppCode(app!!.appCode)
            },
            executorService
        )
    }
}