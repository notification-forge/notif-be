package com.forge.messageservice.resolvers.queries

import com.forge.messageservice.entities.Message
import com.forge.messageservice.entities.Template
import com.forge.messageservice.entities.responses.PluginResponse
import com.forge.messageservice.graphql.CursorResolver
import com.forge.messageservice.graphql.extensions.Connection
import com.forge.messageservice.graphql.models.inputs.PaginationInput
import com.forge.messageservice.resolvers.queries.helpers.GQLConnectionHelper
import com.forge.messageservice.resolvers.queries.responses.PluginResponseConverter
import com.forge.messageservice.services.MessageService
import com.forge.messageservice.services.PluginService
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.relay.DefaultEdge
import org.springframework.stereotype.Component

@Component
class MessageResolver(
    private val messageService: MessageService
) : GraphQLQueryResolver {

    fun message(messageId: Long): Message {
        return messageService.getMessage(messageId)
    }

    fun messages(name: String, appCode: String, pageRequestInput: PaginationInput): Connection<Message> {
        if (pageRequestInput.sortField.isNullOrEmpty()) {
            pageRequestInput.sortField = "lastModifiedDate"
        }
        return GQLConnectionHelper.gqlConnectionFor({
            messageService.getAllMessagesWithTemplateNameAndInAppCode(
                appCode, name, pageRequestInput.asPageRequest(), pageRequestInput.sortField!!
            )
        }) {
            DefaultEdge(it, CursorResolver.from(it.id!!))
        }
    }
}