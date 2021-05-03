package com.forge.messageservice.controllers.v1

import com.forge.messageservice.services.MessageDispatcherService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
     * Sends a notification using the `templateId` specified.
     *
     * @param templateVersionId Identifies the [TemplateVersion](com.forge.messageservice.entities.TemplateVersion) to be used
     * @param messageParameter The parameter map to be used to replace placeholders in the template
     */
    @PostMapping("/{templateVersionId}")
    fun send(
        @PathVariable templateVersionId: String,
        @RequestBody messageParameter: Map<String, Any>
    ): ResponseEntity<String> {
        messageDispatcher.enqueueMessage()
        return ResponseEntity.ok("ok")
    }

}