package com.forge.messageservice.lib

import com.alphamail.plugin.api.AlphamailPlugin

class SomeLib : AlphamailPlugin {

    override fun execute(): Any? {
        TODO("Not yet implemented")
    }

    override fun type(): AlphamailPlugin.PluginType {
        return AlphamailPlugin.PluginType.AfterSend
    }
}