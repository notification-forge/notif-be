package com.forge.messageservice.authentication.ldap

import org.slf4j.LoggerFactory
import org.springframework.ldap.core.DirContextOperations
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
class LdapRolePopulator : LdapAuthoritiesPopulator {
    companion object {
        private val logger = LoggerFactory.getLogger(LdapRolePopulator::class.java)
    }

    private val LDAP_ROLE_PATTERN = "^CN=(?<role>.+?),.*\$"

    override fun getGrantedAuthorities(userData: DirContextOperations, username: String): List<GrantedAuthority> {
        val groups = userData.getStringAttributes("memberOf")?.toSet()
        logger.info("$username has groups = $groups")
        return when {
            groups != null -> {
                val roles = groups.mapNotNull { extractRoleFromGroup(it) }.toList()
                logger.info("$username has role = ${roles.joinToString(" ")}")
                return AuthorityUtils.createAuthorityList(*roles.toTypedArray())
            }
            else -> emptyList()
        }
    }

    private fun extractRoleFromGroup(group: String): String? {
        val matcher = Pattern.compile(LDAP_ROLE_PATTERN).matcher(group)
        return if (matcher.matches()) matcher.group("role") else null
    }


}