package com.forge.messageservice.resolvers.mutations

import com.forge.messageservice.entities.Message
import com.forge.messageservice.graphql.models.inputs.MessageInput
import com.forge.messageservice.services.MessageDispatcherService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component
import javax.validation.Valid

@Component
class MessageMutation(
    private val messageDispatcherService: MessageDispatcherService
) : GraphQLMutationResolver {

    fun createMessage(@Valid input: MessageInput): Message {
        return messageDispatcherService.dispatchMessage(input)
    }
}