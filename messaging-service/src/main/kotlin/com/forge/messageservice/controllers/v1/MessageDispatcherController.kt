package com.forge.messageservice.controllers.v1

import com.forge.messageservice.controllers.v1.api.request.MessageDispatchRequest
import com.forge.messageservice.services.MessageDispatcherService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

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
     * @param templateVersionId Identifies the [TemplateVersion](com.forge.messageservice.entities.TemplateVersion) to be used
     * @param messageParameter The parameter map to be used to replace placeholders in the template
     */
    @PostMapping("/{templateVersionId}")
    fun sendAsync(
        @PathVariable templateVersionId: String,
        @RequestBody messageDispatchRequest: MessageDispatchRequest,
        @RequestPart("attachments", required = false) attachments: Array<MultipartFile>?
    ): ResponseEntity<String> {
        messageDispatcher.enqueueMessage()
        return ResponseEntity.ok("ok")
    }

}