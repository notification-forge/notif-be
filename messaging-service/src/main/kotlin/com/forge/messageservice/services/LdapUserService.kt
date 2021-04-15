package com.forge.messageservice.services

import com.forge.messageservice.authentication.ldap.GroupMemberMapper
import com.forge.messageservice.authentication.ldap.SecurityGroupMembersMapper
import com.forge.messageservice.entities.GroupMember
import org.springframework.context.annotation.Profile
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.query.LdapQueryBuilder.query
import org.springframework.stereotype.Service

interface LdapUserService {
    fun getNameOfUser(username: String): String
    fun getMembersOf(group: String): Set<GroupMember>
    fun getUserDetail(attribute: String, value: String): GroupMember?
}

@Service
@Profile("!dev & !test & !integration-test")
class RealLdapUserService(
    private val ldapTemplate: LdapTemplate
) : LdapUserService {

    override fun getMembersOf(group: String): Set<GroupMember> {
        val query = query()
            .attributes("members")
            .where("cn").`is`(group)
            .and("objectCategory").`is`("GROUP")
        return ldapTemplate.search(query, SecurityGroupMembersMapper())
            .flatMap(::getMembersUserDetails)
            .toSet()
    }

    override fun getUserDetail(attribute: String, value: String): GroupMember? {
        return ldapTemplate.search(
            query()
                .where("cn").`is`(value)
                .and("objectCategory").`is`("PERSON"),
            GroupMemberMapper()
        ).firstOrNull()
    }

    override fun getNameOfUser(username: String): String {
        return getUserDetail("cn", username)?.displayName ?: ""
    }

    private fun getMembersUserDetails(cnList: List<String>): List<GroupMember> {
        return cnList.mapNotNull { m -> getUserDetail("cn", m) }
    }
}

@Service
@Profile("dev | test | integration-test")
class MockLdapUserService(
    private val ldapTemplate: LdapTemplate
) : LdapUserService {

    override fun getMembersOf(group: String): Set<GroupMember> {
        return getMembersUserDetails(
            listOf(
                "notificationforgeusr1",
                "notificationforgeusr2",
                "notificationforgeapvr"
            )
        ).toSet()
    }

    override fun getUserDetail(attribute: String, value: String): GroupMember? {
        when (value) {
            "notificationforgeusr1" -> {
                return GroupMember(
                    "notificationforgeusr1",
                    "Notification Forge User 1",
                    "notificationforgeusr1",
                    "notificationforgeusr1@notification.com"
                )
            }
            "notificationforgeusr2" -> {
                return GroupMember(
                    "notificationforgeusr2",
                    "Notification Forge User 2",
                    "notificationforgeusr2",
                    "notificationforgeusr2@notification.com"
                )
            }
            "notificationforgeapvr" -> {
                return GroupMember(
                    "notificationforgeapvr",
                    "Notification Forge Approver",
                    "notificationforgeapvr",
                    "notificationforgeapvr@notification.com"
                )
            }
        }
        return null
    }

    override fun getNameOfUser(username: String): String {
        return getUserDetail("cn", username)?.displayName ?: ""
    }

    private fun getMembersUserDetails(cnList: List<String>): List<GroupMember> {
        return cnList.mapNotNull { m -> getUserDetail("cn", m) }
    }
}