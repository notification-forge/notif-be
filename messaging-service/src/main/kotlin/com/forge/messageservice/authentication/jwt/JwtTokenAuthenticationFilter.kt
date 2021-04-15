package com.forge.messageservice.authentication.jwt

import com.forge.messageservice.authentication.UserContext
import com.forge.messageservice.controllers.exceptions.APIKeyNotFoundException
import com.forge.messageservice.repositories.ApiClientRepository
import com.forge.messageservice.services.LdapUserService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import org.slf4j.MDC
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.filter.OncePerRequestFilter
import java.text.ParseException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.jvm.Throws

class JwtTokenAuthenticationFilter (
    private val jwtTokenProvider: JwtTokenProvider,
    private val apiClientRepository: ApiClientRepository,
    private val ldapUserService: LdapUserService,
    path: String
): OncePerRequestFilter(){

    private val requestMatcher: RequestMatcher
    private val API_TOKEN_HEADER = "x-api-key"

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            if(hasNoAuthorization(request)){
                filterChain.doFilter(request,response)
                return
            }
            val token: String
            if (hasAuthHeader(request, API_TOKEN_HEADER)){
                token = extractAndDecodeJwtTokenForm(request, API_TOKEN_HEADER)
                if (!apiClientRepository.existsByAccessKey(token)){
                    throw APIKeyNotFoundException("API Key $token has expired or has yet to be registered")
                }
            } else {
                token = extractAndDecodeJwtTokenForm(request, AUTHORIZATION)
            }
            checkAuthenticationAndValidity(token)

            val claims = jwtTokenProvider.getClaims(token)
            val auth = buildAuthenticationFromJwt(claims, request)
            SecurityContextHolder.getContext().authentication = auth
            MDC.put("username", UserContext.loggedInUsername())
            filterChain.doFilter(request, response)
        } catch (e: Exception){
            handleException(e, response)
        } finally {
            SecurityContextHolder.clearContext()
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest) = requestMatcher.matches(request)

    @Throws(ParseException::class)
    private fun extractAndDecodeJwtTokenForm(request: HttpServletRequest, headerKey: String): String{
        val authHeader = request.getHeader(headerKey)
        return authHeader.substring("Bearer".length)
    }

    private fun checkAuthenticationAndValidity(token: String) = jwtTokenProvider.validateToken(token)

    private fun hasNoAuthorization(request: HttpServletRequest) = !(hasAuthHeader(request, AUTHORIZATION) || hasAuthHeader(request, API_TOKEN_HEADER))

    private fun hasAuthHeader(request: HttpServletRequest, headerKey: String): Boolean {
        val header = request.getHeader(headerKey)
        return header != null && header.startsWith("Bearer ")
    }

    private fun handleException(e: Exception, response: HttpServletResponse) {
        when (e) {
            is ExpiredJwtException -> response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token is not valid anymore")
            is UnsupportedJwtException -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unsupported JWT Token")
            is MalformedJwtException -> response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token is malformed")
            else -> throw e
        }
    }

    @Throws(ParseException::class)
    private fun buildAuthenticationFromJwt(claims: Claims, request: HttpServletRequest): Authentication{
        val username = claims.subject
        val roles = claims["roles"] as List<String>
        val authorities = roles.map { role -> SimpleGrantedAuthority(role) }
        val userDetails = JwtUser(username, ldapUserService.getNameOfUser(username), claims.issuedAt)
        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, authorities)
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        return authentication
    }

    init { requestMatcher = AntPathRequestMatcher(path) }

}