package com.forge.messageservice.connections

import graphql.relay.ConnectionCursor
import graphql.relay.DefaultConnectionCursor
import graphql.relay.Edge
import org.springframework.stereotype.Component

@Component
class CursorUtil {

    fun from(id: Long): ConnectionCursor {
        return DefaultConnectionCursor(id.toString())
    }

    fun from(id: String): ConnectionCursor {
        return DefaultConnectionCursor(id)
    }

    fun <T> getFirstCursorFrom(edges: List<Edge<T>>): ConnectionCursor? {
        if (edges.isEmpty()){
            return null
        }
        return edges[0].cursor
    }

    fun <T> getLastCursorFrom(edges: List<Edge<T>>): ConnectionCursor? {
        if (edges.isEmpty()){
            return null
        }
        return edges[edges.size - 1].cursor
    }
}