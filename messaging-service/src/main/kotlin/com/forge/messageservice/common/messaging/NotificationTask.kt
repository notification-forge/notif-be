package com.forge.messageservice.common.messaging

import com.forge.messageservice.entities.Template
import java.io.Serializable

data class NotificationTask(
    val channel: Template.AlertType,
    val message: NotificationMessage
) : Serializable