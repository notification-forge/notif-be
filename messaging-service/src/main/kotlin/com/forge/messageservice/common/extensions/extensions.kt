package com.forge.messageservice.common.extensions

fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }