package com.forge.messageservice.exceptions

import graphql.GraphQLException

class TemplateExistedException(message: String): GraphQLException(message)

class TemplateDoesNotExistException(message: String): GraphQLException(message)

class TemplateVersionExistedException(message: String): GraphQLException(message)

class TemplateVersionDoesNotExistException(message: String): GraphQLException(message)