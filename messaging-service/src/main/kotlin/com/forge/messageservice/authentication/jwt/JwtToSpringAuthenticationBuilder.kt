package com.forge.messageservice.authentication.jwt

import com.forge.messageservice.services.LdapUserService
import io.jsonwebtoken.Claims
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import javax.servlet.http.HttpServletRequest

internal class JwtToSpringAuthenticationBuilder(
    private val ldapUserService: LdapUserService,
    private val jwtTokenProvider: JwtTokenProvider
) {

    private lateinit var tokenHolder: TokenHolder
    private lateinit var request: HttpServletRequest

    fun withToken(tokenHolder: TokenHolder): JwtToSpringAuthenticationBuilder {
        this.tokenHolder = tokenHolder
        return this
    }

    fun withHttpServletRequest(request: HttpServletRequest): JwtToSpringAuthenticationBuilder {
        this.request = request
        return this
    }

    fun build(): Authentication {
        val claims = jwtTokenProvider.getClaims(tokenHolder.token)

        val username = claims.subject
        val roles = claims["roles"] as List<String>
        val authorities = roles.map { role -> SimpleGrantedAuthority(role) }
        val userDetails = createAuthDetailsFor(username, claims)

        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, authorities)
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

        return authentication
    }

    private fun createAuthDetailsFor(username: String, claims: Claims): Any {
        return when (tokenHolder.tokenType) {
            TokenType.USER -> JwtUser(username, ldapUserService.getNameOfUser(username), claims.issuedAt, emptyList())
            TokenType.API_CLIENT -> JwtApiClient(username, username, claims.issuedAt)
        }
    }
}
