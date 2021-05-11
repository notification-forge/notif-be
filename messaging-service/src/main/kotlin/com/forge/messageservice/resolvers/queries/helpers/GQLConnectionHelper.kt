package com.forge.messageservice.resolvers.queries.helpers

import com.forge.messageservice.graphql.CursorResolver
import com.forge.messageservice.graphql.GraphQLConnection
import graphql.relay.DefaultEdge
import graphql.relay.DefaultPageInfo
import org.springframework.data.domain.Page

object GQLConnectionHelper {

    /**
     * Creates a [GraphQLConnection] for entity of type T to a GraphQL entity of U
     */
    fun <T, U> gqlConnectionFor(dataSupplier: () -> Page<T>, entityToGqlTypeMapper: (t: T) -> DefaultEdge<U>): GraphQLConnection<U> {
        val paginatedList = dataSupplier.invoke()

        val edges = paginatedList.content.map { image -> entityToGqlTypeMapper.invoke(image) }

        return GraphQLConnection(
            paginatedList.totalElements.toInt(),
            edges,
            DefaultPageInfo(
                CursorResolver.startCursor(edges),
                CursorResolver.endCursor(edges),
                paginatedList.hasPrevious(),
                paginatedList.hasNext()
            )
        )
    }

}