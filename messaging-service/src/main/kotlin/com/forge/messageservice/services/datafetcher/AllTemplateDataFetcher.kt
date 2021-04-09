package com.forge.messageservice.services.datafetcher

import com.forge.messageservice.entity.Template
import com.forge.messageservice.repositories.TemplateRepository
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component
class AllTemplateDataFetcher(
    private val templateRepository: TemplateRepository
) : DataFetcher<List<Template>> {

    override fun get(dataFetchingEnvironment: DataFetchingEnvironment): List<Template> {
        return templateRepository.findAll()
    }
}