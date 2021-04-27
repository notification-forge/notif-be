package com.forge.messageservice.services

import com.forge.messageservice.entities.Plugin
import com.forge.messageservice.exceptions.PluginMissingException
import com.forge.messageservice.repositories.PluginRepository
import org.springframework.stereotype.Service

@Service
class PluginService(private val pluginRepository: PluginRepository) {

    fun findAvailablePluginsForApp(appCode: String): List<Plugin> {
        return pluginRepository.findAllByAppCode(appCode)
    }

    fun ensurePluginExist(pluginId: Long) {
        val optionalPlugin = pluginRepository.findById(pluginId)
        if (optionalPlugin.isEmpty){
            throw PluginMissingException("Unable to find Plugin $pluginId")
        }
    }

    fun getPlugin(pluginId: Long): Plugin{
        val optionalPlugin = pluginRepository.findById(pluginId)
        if (optionalPlugin.isEmpty){
            throw PluginMissingException("Unable to find Plugin $pluginId")
        }
        return optionalPlugin.get()
    }
}