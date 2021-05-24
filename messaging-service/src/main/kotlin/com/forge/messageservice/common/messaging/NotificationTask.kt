package com.forge.messageservice.common.messaging

import com.forge.messageservice.entities.MailSettings
import com.forge.messageservice.entities.TeamsSettings
import java.io.Serializable

data class MailNotificationTask(
    val messageId: Long,
    val mailSettings: MailSettings,
    val body: String
) : Serializable

data class TeamsNotificationTask(
    val messageId: Long,
    val teamSettings: TeamsSettings,
    val body: String
) : Serializable