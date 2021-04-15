package com.forge.messageservice.repositories

import com.forge.messageservice.entities.ApiClient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ApiClientRepository : JpaRepository<ApiClient, String>{
    fun existsByAccessKey(accessKey: String): Boolean
}