package com.forge.messageservice.controllers.v1

import com.forge.messageservice.authentication.UserContext
import com.forge.messageservice.controllers.v1.api.response.OnboardingResponse
import com.forge.messageservice.services.OnboardingService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1")
class UserProfileController(
    private val onboardingService: OnboardingService
) {
    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @GetMapping("/whoami")
    fun whoami(): ResponseEntity<Any> {
        logger.info("Received request for whoami")
        val jwtUser = UserContext.loggedInUser()

        jwtUser.apps = onboardingService.getAppsOwnsByUser(UserContext.loggedInUsername()).map {
            OnboardingResponse(
                it.appCode, it.displayName!!, it.apiToken!!, it.status
            )
        }
        return ResponseEntity.ok(jwtUser)
    }
}