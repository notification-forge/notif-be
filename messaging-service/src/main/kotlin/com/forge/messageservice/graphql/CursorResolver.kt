package com.forge.messageservice.graphql

import com.forge.messageservice.common.extensions.asBase64String
import graphql.relay.ConnectionCursor
import graphql.relay.DefaultConnectionCursor
import graphql.relay.Edge

object CursorResolver {

    fun from(id: Long): ConnectionCursor {
        return DefaultConnectionCursor(id.toString().asBase64String())
    }

    fun <T> startCursor(edges: List<Edge<T>>): ConnectionCursor? {
        return if (edges.isEmpty()) return null
        else edges.first().cursor
    }

    fun <T> endCursor(edges: List<Edge<T>>): ConnectionCursor? {
        return if (edges.isEmpty()) return null
        else edges.last().cursor
    }
}