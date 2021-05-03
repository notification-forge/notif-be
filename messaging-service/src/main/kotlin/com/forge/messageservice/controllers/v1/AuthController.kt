package com.forge.messageservice.controllers.v1

import com.forge.messageservice.authentication.jwt.JwtAuthenticationResponse
import com.forge.messageservice.authentication.jwt.JwtTokenProvider
import com.forge.messageservice.controllers.v1.api.request.LoginRequest
import com.forge.messageservice.controllers.v1.api.response.ApiResponse
import com.forge.messageservice.entities.User
import com.forge.messageservice.repositories.UserRepository
import com.forge.messageservice.services.RegistrationService
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
    private val registrationService: RegistrationService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository
) {
    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        if (loginRequest.username.isEmpty() || loginRequest.password.isEmpty()) {
            return ResponseEntity<Any>(ApiResponse(false, "Invalid username or password"), HttpStatus.BAD_REQUEST)
        }

        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.username,
                loginRequest.password
            )
        )
        val userOpt = userRepository.findById(loginRequest.username)
        val user = if (userOpt.isEmpty) userRepository.save(User().apply { username=loginRequest.username }) else userOpt.get()
        // creates tenant and tenant user mapping if necessary
        registrationService.registerMissingApps(user, authentication)

        val jwt: String = jwtTokenProvider.generateToken(authentication)
        return ResponseEntity.ok(JwtAuthenticationResponse(jwt))
    }
}