package com.forge.messageservice.graphql.extensions

import graphql.relay.Connection

interface Connection<T> : Connection<T>{

    fun totalCount(): Int
    
}