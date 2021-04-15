package com.forge.messageservice.configurations

import com.forge.messageservice.authentication.jwt.JwtAuthenticationEntryPoint
import com.forge.messageservice.authentication.jwt.JwtTokenAuthenticationFilter
import com.forge.messageservice.authentication.jwt.JwtTokenProvider
import com.forge.messageservice.authentication.ldap.LdapRolePopulator
import com.forge.messageservice.logging.LoggingDiagnosticFilter
import com.forge.messageservice.repositories.ApiClientRepository
import com.forge.messageservice.services.LdapUserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.ldap.core.support.LdapContextSource
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.access.ExceptionTranslationFilter
import kotlin.jvm.Throws

@Configuration
open class LDAPConfigHolder {
    @Value("\${ldap.user.base:}")
    lateinit var userSearchBase: String

    @Value("\${ldap.user.filter:sAMAccountName={0}}")
    lateinit var userSearchFilter: String
}

@Configuration
open class SecurityConfigHolder {
    @Value("\${app.auth.auth-free.urls}")
    lateinit var authFreeURLs: Array<String>

    @Value("\${app.auth.groups-permitted}")
    lateinit var groups: Array<String>
}

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@Profile("!dev & !test & !integration-test")
open class WebSecurityConfigurations(
    private val jwtTokenProvider: JwtTokenProvider,
    private val contextSource: LdapContextSource,
    private val unauthorizedHandler: JwtAuthenticationEntryPoint,
    private val apiClientRepository: ApiClientRepository,
    private val securityConfigHolder: SecurityConfigHolder,
    private val ldapSearchConfigHolder: LDAPConfigHolder,
    private val ldapUserService: LdapUserService
) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .cors().disable()
            .addFilterAfter(LoggingDiagnosticFilter(), ExceptionTranslationFilter::class.java)
            .addFilterAfter(
                JwtTokenAuthenticationFilter(
                    jwtTokenProvider,
                    apiClientRepository,
                    ldapUserService,
                    "api/v1/auth/**"
                ), LoggingDiagnosticFilter::class.java
            )
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(unauthorizedHandler)
            .and()
            .authorizeRequests()
            .antMatchers(*securityConfigHolder.authFreeURLs).permitAll()

            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated()
    }

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


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@Profile("dev | test | integration-test")
open class LocalSecurityConfigurations(
    private val jwtTokenProvider: JwtTokenProvider,
    private val unauthorizedHandler: JwtAuthenticationEntryPoint,
    private val apiClientRepository: ApiClientRepository,
    private val securityConfigHolder: SecurityConfigHolder,
    private val ldapUserService: LdapUserService
) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .cors().disable()
            .addFilterAfter(LoggingDiagnosticFilter(), ExceptionTranslationFilter::class.java)
            .addFilterAfter(
                JwtTokenAuthenticationFilter(
                    jwtTokenProvider,
                    apiClientRepository,
                    ldapUserService,
                    "api/v1/auth/**"
                ), LoggingDiagnosticFilter::class.java
            )
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(unauthorizedHandler)
            .and()
            .authorizeRequests()
            .antMatchers(*securityConfigHolder.authFreeURLs).permitAll()

            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated()
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
            .withUser("notificationforgeusr1")
            .password("{noop}secret")
            .roles("NOTIFICATION_FORGE")
            .and()
            .withUser("notificationforgeusr2")
            .password("{noop}secret")
            .roles("NOTIFICATION_FORGE")
            .and()
            .withUser("notificationforgeapvr")
            .password("{noop}secret")
            .roles("NOTIFICATION_FORGE")
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

}