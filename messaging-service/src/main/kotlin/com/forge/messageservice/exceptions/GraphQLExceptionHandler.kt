package com.forge.messageservice.exceptions

import graphql.kickstart.execution.error.GraphQLErrorHandler
import graphql.ExceptionWhileDataFetching
import graphql.GraphQLError
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class GraphQLExceptionHandler : GraphQLErrorHandler {
    override fun processErrors(list: List<GraphQLError>): List<GraphQLError> {
        return list.stream().map { error: GraphQLError -> getNested(error) }.collect(Collectors.toList())
    }

    private fun getNested(error: GraphQLError): GraphQLError {
        if (error is ExceptionWhileDataFetching) {
            val exceptionError = error
            if (exceptionError.exception is GraphQLError) {
                return exceptionError.exception as GraphQLError
            }
        }
        return error
    }
}