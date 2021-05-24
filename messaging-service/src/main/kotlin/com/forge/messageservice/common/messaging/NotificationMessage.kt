package com.forge.messageservice.common.messaging

import java.io.Serializable

data class Recipients(
    val to: List<String>,
    val cc: List<String> = emptyList(),
    val bcc: List<String> = emptyList()
) : Serializable