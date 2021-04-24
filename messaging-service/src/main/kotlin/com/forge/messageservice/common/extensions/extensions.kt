package com.forge.messageservice.common.extensions

import java.util.*

fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

fun String.asBase64String() = Base64.getEncoder().encode(this.toByteArray()).toHexString()