package com.forge.messageservice.repositories

import com.forge.messageservice.entities.Onboarding
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OnboardingRepository : JpaRepository<Onboarding, Long> {
    fun findAllByAppCode(appCode: String): List<Onboarding>
    fun findAllByUsername(username: String): List<Onboarding>
}