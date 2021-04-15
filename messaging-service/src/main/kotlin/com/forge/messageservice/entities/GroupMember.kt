package com.forge.messageservice.entities

data class GroupMember(
    val id: String,
    val displayName: String,
    val commonName: String,
    val mail: String?
)