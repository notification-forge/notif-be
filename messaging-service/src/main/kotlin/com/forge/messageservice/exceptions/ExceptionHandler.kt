package com.forge.messageservice.exceptions

import graphql.GraphQLException
import graphql.kickstart.spring.error.ThrowableGraphQLError
import org.hibernate.exception.ConstraintViolationException
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.RuntimeException

@Component
class ExceptionHandler{

    @ExceptionHandler(GraphQLException::class, ConstraintViolationException::class)
    fun handle(e: GraphQLException): ThrowableGraphQLError{
        return ThrowableGraphQLError(e)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handle(e: RuntimeException): ThrowableGraphQLError{
        return ThrowableGraphQLError(e, "Internal Server Error")
    }
}