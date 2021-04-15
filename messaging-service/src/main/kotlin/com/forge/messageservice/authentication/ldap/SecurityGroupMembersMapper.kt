package com.forge.messageservice.authentication.ldap

import org.springframework.ldap.core.AttributesMapper
import javax.naming.directory.Attributes

private val userDnPattern = "CN=(?<cn>[A-Za-z0-9_]+)".toRegex()

class SecurityGroupMembersMapper : AttributesMapper<List<String>> {

    override fun mapFromAttributes(attributes: Attributes): List<String> {
        val result = mutableListOf<String>()

        val members = attributes["member"].all
        while (members.hasMoreElements()) {
            val cn = members.next() as String
            val match = userDnPattern.find(cn)
            if (match != null) {
                result.add(match.groups["cn"]!!.value)
            }
        }
        return result
    }

}