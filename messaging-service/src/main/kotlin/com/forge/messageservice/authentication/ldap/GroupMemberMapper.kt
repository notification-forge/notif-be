package com.forge.messageservice.authentication.ldap

import com.forge.messageservice.entities.GroupMember
import org.springframework.ldap.core.ContextMapper
import org.springframework.ldap.core.DirContextAdapter

class GroupMemberMapper: ContextMapper<GroupMember>{

    override fun mapFromContext(ctx: Any?): GroupMember {
        val dir = ctx as DirContextAdapter
        return GroupMember(
            dir.getStringAttribute("sAMAccountName"),
            dir.getStringAttribute("displayName"),
            dir.getStringAttribute("cn"),
            dir.getStringAttribute("mail")
        )
    }

}