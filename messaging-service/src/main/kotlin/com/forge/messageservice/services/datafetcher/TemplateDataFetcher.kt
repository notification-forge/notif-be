package com.forge.messageservice.services.datafetcher

import com.forge.messageservice.entity.Template
import com.forge.messageservice.repositories.TemplateRepository
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component
class TemplateDataFetcher(
    private val templateRepository: TemplateRepository
) : DataFetcher<Template> {

    override fun get(dataFetchingEnvironment: DataFetchingEnvironment): Template {
        val id = dataFetchingEnvironment.getArgument<Long>("id")
        return templateRepository.getOne(id)
    }
}