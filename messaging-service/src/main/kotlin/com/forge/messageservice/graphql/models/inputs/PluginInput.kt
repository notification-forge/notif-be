package com.forge.messageservice.graphql.models.inputs

data class PluginsInput(
    val plugins: List<PluginInput>
)

data class PluginInput(
    val pluginId: Long,
    val configurations: List<ConfigurationInput>
)

data class ConfigurationInput(
    val key: String,
    val value: String
)