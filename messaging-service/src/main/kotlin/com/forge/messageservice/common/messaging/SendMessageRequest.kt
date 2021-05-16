package com.forge.messageservice.common.messaging

import java.util.*

data class SendMessageRequest(
    val sendersAppCode: String,
    val templateUUID: UUID,
    val templateVersionId: Long,
    val recipients: Recipients,
    val messageParams: Map<String, Any>,
    val subject: String?
)
