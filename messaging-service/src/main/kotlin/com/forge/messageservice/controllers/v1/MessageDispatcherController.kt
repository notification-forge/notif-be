package com.forge.messageservice.controllers.v1

import com.forge.messageservice.services.MessageDispatcherService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
    fun send(@PathVariable templateId: String): ResponseEntity<String> {
        messageDispatcher.dispatch()
        return ResponseEntity.ok("ok")
    }

}