package com.forge.messageservice.exceptions;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;

/** https://betterprogramming.pub/error-handling-with-graphql-spring-boot-and-kotlin-ed55f9da4221
 *
Java Class created in place of Kotlin Class as it cannot extend RuntimeException and implement GraphQLError at the same time */

public class GraphQLException extends RuntimeException implements GraphQLError {

    String customMessage;

    public GraphQLException(String customMessage) {
        this.customMessage = customMessage;
    }

    @Override
    public String getMessage() {
        return customMessage;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorClassification getErrorType() {
        return null;
    }
}