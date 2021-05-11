package com.alphamail.plugin.api

interface AlphamailPlugin {

    fun execute(messageDetails: MessageDetails): Any?

    /**
     * Defines the type of the plugin whether it can be executed before, after or before and after
     * a notification is dispatched
     */
    fun type(): PluginType

    fun runsBefore() : Boolean = type() == PluginType.BeforeSend || type() == PluginType.Both

    fun runsAfter() : Boolean = type() == PluginType.AfterSend || type() == PluginType.Both

    enum class PluginType {
        BeforeSend, AfterSend, Both
    }
}