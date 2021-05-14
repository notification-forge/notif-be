package com.forge.messageservice.services

import com.alphamail.plugin.api.AlphamailPlugin
import com.alphamail.plugin.api.PluginConfiguration
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.forge.messageservice.entities.Plugin
import com.forge.messageservice.exceptions.PluginMissingException
import com.forge.messageservice.lib.kafka.KafkaConfiguration
import com.forge.messageservice.repositories.PluginRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.net.URL
import java.net.URLClassLoader
import kotlin.jvm.internal.Reflection
import kotlin.reflect.KClass

@Service
class PluginService(
    private val pluginRepository: PluginRepository,
    private val objectMapper: ObjectMapper
) {

    @Transactional(readOnly = true)
    fun findAvailablePluginsForApp(appCode: String): List<Plugin> {
        return pluginRepository.findAllByAppCode(appCode)
    }

    fun ensurePluginExist(pluginId: Long) {
        val optionalPlugin = pluginRepository.findById(pluginId)
        if (optionalPlugin.isEmpty) {
            throw PluginMissingException("Unable to find Plugin $pluginId")
        }
    }

    fun getPlugin(pluginId: Long): Plugin {
        val optionalPlugin = pluginRepository.findById(pluginId)
        if (optionalPlugin.isEmpty) {
            throw PluginMissingException("Unable to find Plugin $pluginId")
        }
        return optionalPlugin.get()
    }

    fun loadPlugin(plugin: Plugin, pluginConfiguration: String): AlphamailPlugin {
        val child = URLClassLoader(arrayOf(URL(plugin.jarUrl)), this.javaClass.classLoader)

        val classToLoad = Class.forName(plugin.pluginClassName, true, child)
        val kClass = Reflection.createKotlinClass(classToLoad)
        val parameterType = classToLoad.constructors[0]!!.parameters[0]!!.parameterizedType

        val parameterClass = Class.forName(parameterType.typeName, true, child)

        val configuration = objectMapper.readValue(pluginConfiguration, parameterClass)
        return kClass.createInstance(configuration) as AlphamailPlugin
    }

    //Decorator to call create plugin instance with config
    private fun <T : Any> KClass<T>.createInstance(configuration: Any): T {
//    Can use filter here if you have more than 1 constructor
        val noArgsConstructor = constructors.first()

//    If decided every plugin doesn't have other param, then this is good enough
        val params = noArgsConstructor.parameters.associateWith { configuration }

//    Invoke constructor with param
        return noArgsConstructor.callBy(params)
    }
}