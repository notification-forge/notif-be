package com.forge.messageservice.lib

import com.alphamail.plugin.api.AlphamailPlugin
import com.alphamail.plugin.api.MessageDetails

class SomeLib : AlphamailPlugin {

    override fun beforeSend(message: MessageDetails): Any? {
        return "Hello"
    }

    override fun afterSend(message: MessageDetails): Any? {
        TODO("Not yet implemented")
    }
}