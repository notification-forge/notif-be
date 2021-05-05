package com.alphamail.plugin.api

interface AlphamailPlugin {

    fun beforeSend(message: MessageDetails): Any?

    fun afterSend(message: MessageDetails): Any?
}