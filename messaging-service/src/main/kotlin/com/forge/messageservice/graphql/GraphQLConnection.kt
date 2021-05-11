package com.forge.messageservice.graphql

import com.forge.messageservice.graphql.extensions.Connection
import graphql.relay.DefaultConnection
import graphql.relay.Edge
import graphql.relay.PageInfo

class GraphQLConnection<T>(private val totalCount: Int, edges: List<Edge<T>>, pageInfo: PageInfo) :
    DefaultConnection<T>(edges, pageInfo), Connection<T> {

    override fun totalCount(): Int {
        return totalCount
    }
}