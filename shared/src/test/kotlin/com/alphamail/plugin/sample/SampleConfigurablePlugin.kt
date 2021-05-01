package com.alphamail.plugin.sample

import com.alphamail.plugin.api.AlphamailPlugin

/**
 * A sample plugin demonstrating a plugin with required configuration
 */
class SampleConfigurablePlugin(cfg: Configuration) : AlphamailPlugin {

    override fun execute(): Any? {
        TODO("Not yet implemented")
    }

    override fun type(): AlphamailPlugin.PluginType {
        return AlphamailPlugin.PluginType.Both
    }
}