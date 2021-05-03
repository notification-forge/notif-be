package com.alphamail.plugin.api

interface AlphamailPlugin {

    fun execute(): Any?

    /**
     * Defines the type of the plugin whether it can be executed before, after or before and after
     * a notification is dispatched
     */
    fun type(): PluginType

    enum class PluginType {
        BeforeSend, AfterSend, Both
    }
}