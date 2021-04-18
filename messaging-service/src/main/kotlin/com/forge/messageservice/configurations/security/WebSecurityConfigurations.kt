package com.forge.messageservice.configurations.security

import com.forge.messageservice.authentication.jwt.JwtAuthenticationEntryPoint
import com.forge.messageservice.authentication.jwt.JwtTokenProvider
import com.forge.messageservice.authentication.ldap.LdapRolePopulator
import com.forge.messageservice.repositories.ApiClientRepository
import com.forge.messageservice.services.LdapUserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.ldap.core.support.LdapContextSource
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@Profile("!dev & !test & !integration-test")
open class WebSecurityConfigurations(
    jwtTokenProvider: JwtTokenProvider,
    private val contextSource: LdapContextSource,
    unauthorizedHandler: JwtAuthenticationEntryPoint,
    apiClientRepository: ApiClientRepository,
    securityConfigHolder: SecurityConfigHolder,
    private val ldapSearchConfigHolder: LDAPConfigHolder,
    ldapUserService: LdapUserService
) : DevSecurityConfigurations(
    jwtTokenProvider,
    unauthorizedHandler,
    apiClientRepository,
    securityConfigHolder,
    ldapUserService
) {

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.ldapAuthentication()
            .userSearchBase(ldapSearchConfigHolder.userSearchBase)
            .userSearchFilter(ldapSearchConfigHolder.userSearchFilter)
            .ldapAuthoritiesPopulator(LdapRolePopulator())
            .contextSource(contextSource)
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}
