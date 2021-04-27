package com.forge.messageservice.repositories

import com.forge.messageservice.entities.Plugin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PluginRepository : JpaRepository<Plugin, Long> {

    @Query("select p from Plugin p where lower(p.appCode) = lower(:appCode) or p.appCode IS NULL")
    fun findAllByAppCode(appCode: String): List<Plugin>

}