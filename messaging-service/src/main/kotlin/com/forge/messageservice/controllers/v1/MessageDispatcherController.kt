package com.forge.messageservice.controllers.v1

import com.forge.messageservice.services.MessageDispatcherService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 *
 */
@RestController
@RequestMapping("/v1/messages")
class MessageDispatcherController(
    private val messageDispatcher: MessageDispatcherService
) {

    /**
     * Sends a notification using the `templateId` specified.
     */
    @PostMapping("/{templateId}")
    fun send(@PathVariable templateId: String, @RequestBody messageParameter: Map<String, Any>): ResponseEntity<String> {
        messageDispatcher.enqueueMessage()
        return ResponseEntity.ok("ok")
    }

}