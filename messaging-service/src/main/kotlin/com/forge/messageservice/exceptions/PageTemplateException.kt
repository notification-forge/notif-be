package com.forge.messageservice.exceptions

import graphql.ErrorClassification
import graphql.ErrorType

open class PageTemplateException(
    errorMessage: String? = "",
    private val parameters: Map<String, Any>? = mutableMapOf()
) : GraphQLException(errorMessage) {
    override val message: String?
        get() = super.message

    override fun getExtensions(): MutableMap<String, Any> {
        return mutableMapOf("parameters" to (parameters ?: mutableMapOf()))
    }

    override fun getErrorType(): ErrorClassification {
        return ErrorType.DataFetchingException
    }
}