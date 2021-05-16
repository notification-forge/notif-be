package com.forge.messageservice.controllers.v1

import com.forge.messageservice.authentication.UserContext
import com.forge.messageservice.common.messaging.SendMessageRequest
import com.forge.messageservice.controllers.v1.api.request.MessageDispatchRequest
import com.forge.messageservice.services.MessageDispatcherService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

/**
 * This controller handles incoming messages from clients. Messages received by this controller
 * are delivered to the delivery channels as defined in the [Templates](com.forge.messageservice.entities.Template)
 */
@RestController
@RequestMapping("/v1/messages")
class MessageDispatcherController(
    private val messageDispatcher: MessageDispatcherService
) {

    /**
     * Sends a notification asynchronously using the specified `templateVersionId`.
     *
     * The `sendAsync` function enqueues notification messages as a [NotificationTask](com.forge.messageservice.common.messaging.NotificationTask)
     * in kafka and are consumed by dispatcher workers separately (@see [MessageDispatcherService] for more details).
     *
     * This implementation is largely inspired by Python's Celery
     *
     * Usage:
     * /v1/messages/{templateUUID}/{templateVersionId}
     *
     * Example:
     * /v1/messages/50c3a8c0-15a0-4ac5-800e-a66f7fb287f7/1
     *
     * @param templateUUID Identifies the family of template versions to be used
     * @param templateVersionId Identifies the [TemplateVersion](com.forge.messageservice.entities.TemplateVersion) to be used
     * @param messageDispatchRequest Contains the request details e.g. parameter map, recipients, etc
     */
    @PostMapping("/{templateUUID}/{templateVersionId}")
    fun sendAsync(
        @PathVariable templateUUID: String,
        @PathVariable templateVersionId: Long,
        @RequestBody messageDispatchRequest: MessageDispatchRequest,
        @RequestPart("attachments", required = false) attachments: Array<MultipartFile>?
    ): ResponseEntity<String> {

        messageDispatcher.enqueueMessage(
            SendMessageRequest(
                sendersAppCode = UserContext.apiClient().applicationCode,
                templateUUID = UUID.fromString(templateUUID),
                templateVersionId = templateVersionId,
                recipients = messageDispatchRequest.recipients,
                messageParams = messageDispatchRequest.parameters,
                subject = messageDispatchRequest.subject
            )
        )
        return ResponseEntity.ok("ok")
    }


}