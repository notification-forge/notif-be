package com.forge.messageservice.controllers.v1

import com.forge.messageservice.authentication.jwt.JwtAuthenticationResponse
import com.forge.messageservice.authentication.jwt.JwtTokenProvider
import com.forge.messageservice.configurations.security.SecurityConfigHolder
import com.forge.messageservice.controllers.v1.api.request.LoginRequest
import com.forge.messageservice.controllers.v1.api.response.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val securityConfigHolder: SecurityConfigHolder
) {
    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any>{
        if (loginRequest.username.isEmpty() || loginRequest.password.isEmpty()){
            return ResponseEntity<Any>(ApiResponse(false, "Invalid username or password"), HttpStatus.BAD_REQUEST)
        }
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.username,
                loginRequest.password
            )
        )

        val groupsAllowed = (securityConfigHolder.groups).toMutableSet()
        groupsAllowed.retainAll(authentication.authorities.map { it.authority.replace(Regex("^ROLE_"), "") })
        logger.info("User groups in notification forge: $groupsAllowed")

        if (groupsAllowed.isEmpty()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        val jwt: String = jwtTokenProvider.generateToken(authentication)
        return ResponseEntity.ok(JwtAuthenticationResponse(jwt))
    }
}