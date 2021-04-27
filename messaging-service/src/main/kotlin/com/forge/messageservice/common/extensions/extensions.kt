package com.forge.messageservice.common.extensions

import org.springframework.util.Base64Utils
import java.nio.charset.Charset

fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }
fun ByteArray.toBase64String() = Base64Utils.encodeToString(this)
fun String.asBase64String(): String = Base64Utils.encodeToString(this.toByteArray(Charset.defaultCharset()))