package com.forge.messageservice.services

import com.alphamail.plugin.api.FieldConfiguration
import com.alphamail.plugin.api.PluginConfiguration
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.forge.messageservice.common.mapper.PluginSettingToConfigurationEntityMapper
import com.forge.messageservice.entities.TemplatePlugin
import com.forge.messageservice.graphql.models.inputs.ConfigurationInput
import com.forge.messageservice.graphql.models.inputs.PluginInput
import com.forge.messageservice.graphql.models.inputs.PluginsInput
import com.forge.messageservice.exceptions.FieldValidationException
import com.forge.messageservice.exceptions.TemplatePluginMissingException
import com.forge.messageservice.repositories.TemplatePluginRepository
import org.springframework.stereotype.Service
import java.net.URL
import java.net.URLClassLoader

@Service
open class TemplatePluginService(
    private val templatePluginRepository: TemplatePluginRepository,
    private val pluginService: PluginService,
    private val objectMapper: ObjectMapper,
    private val entityMapper: PluginSettingToConfigurationEntityMapper
) {

    fun getTemplatePluginsByTemplateVersionId(templateVersionId: Long): List<TemplatePlugin> {
        return templatePluginRepository.findAllByTemplateVersionId(templateVersionId)
    }

    fun getTemplatePluginsByTemplateVersionIdAndPluginId(templateVersionId: Long, pluginId: Long): TemplatePlugin {
        return templatePluginRepository.findAllByTemplateVersionIdAndPluginId(templateVersionId, pluginId)
            ?: throw TemplatePluginMissingException("Unable to find plugin with template version $templateVersionId and plugin $pluginId")
    }

    fun getTemplatePlugin(templatePluginId: Long): TemplatePlugin{
        val optionalTemplatePlugin = templatePluginRepository.findById(templatePluginId)
        return optionalTemplatePlugin.get()
    }

    open fun createTemplatePlugins(templateVersionId: Long, plugins: List<PluginInput>): List<TemplatePlugin> {
        return plugins.map{pluginInput ->
            pluginService.ensurePluginExist(pluginInput.pluginId)

            templatePluginRepository.save(TemplatePlugin().apply {
                this.templateVersionId = templateVersionId
                pluginId = pluginInput.pluginId
                configuration = objectMapper.writeValueAsString(convertPluginConfiguration(pluginInput))
            })
        }
    }

    private fun convertPluginConfiguration(pluginInput: PluginInput): PluginConfiguration{
        val plugin = pluginService.getPlugin(pluginInput.pluginId)
        val fieldConfigurations: List<FieldConfiguration> = objectMapper.readValue(plugin.configurationDescriptors!!)
        val fieldConfigurationMap = fieldConfigurations.map {fieldConfiguration ->
            fieldConfiguration.name to fieldConfiguration
        }.toMap()

        val configurationMap = pluginInput.configurations.map { configurationInput ->
            configurationInput.key to configurationInput.value
        }.toMap()

        val child = URLClassLoader(arrayOf(URL(plugin.jarUrl)), this.javaClass.classLoader)

        try {
            val entityInstance = entityMapper.settingToConfigurationObject(
                configurationMap,
                fieldConfigurationMap,
                Class.forName(plugin.configurationClassName, true, child)
            )
            return entityInstance as PluginConfiguration
        } catch (e: ClassNotFoundException){
            throw FieldValidationException("Unable to find configuration class")
        }
    }

    private fun validateMandatory(configurationInput: ConfigurationInput){
        if (configurationInput.value.isEmpty()){
            throw FieldValidationException("Field ${configurationInput.key} has Value ${configurationInput.value} is empty when it is supposed to be mandatory")
        }
    }

}