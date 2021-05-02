package com.forge.messageservice.configurations.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
open class LDAPConfigHolder {
    @Value("\${ldap.user.base:}")
    lateinit var userSearchBase: String

    @Value("\${ldap.user.filter:sAMAccountName={0}}")
    lateinit var userSearchFilter: String

    @Value("\${ldap.groupSearchBase.filter}")
    lateinit var groupSearchBase: String
}

@Configuration
open class SecurityConfigHolder {
    @Value("\${app.auth.auth-free.urls}")
    lateinit var authFreeURLs: Array<String>

    @Value("\${app.auth.group.pattern:ROLE_U01REG1_(DCIF|ABC)_(\\\\S+)_(APPLEAD|DEVELOPER)}")
    lateinit var groupPattern: String
}