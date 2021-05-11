package com.forge.messageservice.configurations.security

import com.forge.messageservice.authentication.jwt.JwtAuthenticationEntryPoint
import com.forge.messageservice.authentication.jwt.JwtTokenAuthenticationFilter
import com.forge.messageservice.authentication.jwt.JwtTokenProvider
import com.forge.messageservice.logging.LoggingDiagnosticFilter
import com.forge.messageservice.repositories.ApiClientRepository
import com.forge.messageservice.services.LdapUserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
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
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@Profile("dev | test | integration-test")
class DevSecurityConfigurations(
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
            .withUser("ntfusr1")
            .password("{noop}secret")
            .roles("NOTIFICATION_FORGE")
            .and()
            .withUser("ntfusr2")
            .password("{noop}secret")
            .roles("NOTIFICATION_FORGE")
            .and()
            .withUser("ntfapvr")
            .password("{noop}secret")
            .roles("NOTIFICATION_FORGE")
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

}