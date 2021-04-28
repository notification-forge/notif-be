package com.forge.messageservice.resolvers.mutations

import com.forge.messageservice.entities.Message
import com.forge.messageservice.graphql.models.inputs.MessageInput
import com.forge.messageservice.services.MessageService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component
import javax.validation.Valid

@Component
class MessageMutation(
    private val messageService: MessageService
) : GraphQLMutationResolver {

    fun createMessage(@Valid input: MessageInput): Message {
        return messageService.saveMessage(input)
    }
}