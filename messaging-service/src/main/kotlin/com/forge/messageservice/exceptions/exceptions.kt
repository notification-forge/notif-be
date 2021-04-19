package com.forge.messageservice.exceptions


import graphql.GraphQLException

class TemplateExistedException(message: String) : GraphQLException(message)
class TemplateDoesNotExistException(message: String) : GraphQLException(message)
class TemplateVersionExistedException(message: String) : GraphQLException(message)
class TemplateHashExistedException(message: String) : GraphQLException(message)
class TemplateVersionDoesNotExistException(message: String) : GraphQLException(message)

open class BusinessException(msg: String) : Exception(msg)
class TenantAlreadyExistException(msg: String) : BusinessException(msg)
class TenantNotFoundException(msg: String) : BusinessException(msg)