package com.alphamail.plugin.sample

import com.alphamail.plugin.api.AlphamailPlugin
import com.alphamail.plugin.api.MessageDetails

/**
 * A sample plugin demonstrating a plugin with required configuration
 */
class SampleConfigurablePlugin(cfg: Configuration) : AlphamailPlugin {

    override fun execute(messageDetails: MessageDetails): Any? {
        TODO("Not yet implemented")
    }

    override fun type(): AlphamailPlugin.PluginType {
        return AlphamailPlugin.PluginType.Both
    }
}