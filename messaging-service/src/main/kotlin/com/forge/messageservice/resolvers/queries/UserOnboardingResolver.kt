package com.forge.messageservice.resolvers.queries

import com.forge.messageservice.entities.Onboarding
import com.forge.messageservice.entities.User
import com.forge.messageservice.services.OnboardingService
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.function.Supplier

@Component
class UserOnboardingResolver(
    private val onboardingService: OnboardingService
) : GraphQLResolver<User> {

    private val executorService = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors()
    )

    fun onboardings(user: User?): CompletableFuture<List<Onboarding>>? {
        return CompletableFuture.supplyAsync(
            Supplier {
                onboardingService.getOnboardingsByUsername(user!!.username)
            },
            executorService
        )
    }
}