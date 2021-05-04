package com.forge.messageservice.controllers.v1

import com.forge.messageservice.authentication.UserContext
import com.forge.messageservice.authentication.jwt.JwtUser
import com.forge.messageservice.controllers.v1.api.response.TenantResponse
import com.forge.messageservice.repositories.TenantUserRepo
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1")
class UserProfileController(
    private val tenantUserRepo: TenantUserRepo
) {
    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @GetMapping("/whoami")
    fun whoami(): ResponseEntity<JwtUser> {
        logger.info("Received request for whoami")
        val jwtUser = UserContext.loggedInUser()
        jwtUser.apps = tenantUserRepo.findByUsername(jwtUser.username)
            .map { it.tenant!! }
            .map { TenantResponse(it.appCode, it.displayName, it.apiToken, it.status)}
        return ResponseEntity.ok(jwtUser)
    }
}