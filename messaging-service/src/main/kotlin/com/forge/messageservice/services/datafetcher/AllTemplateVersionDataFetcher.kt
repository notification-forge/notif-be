package com.forge.messageservice.services.datafetcher

import com.forge.messageservice.entity.TemplateVersion
import com.forge.messageservice.repositories.TemplateVersionRepository
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component
class AllTemplateVersionDataFetcher(
    private val templateVersionRepository: TemplateVersionRepository
) : DataFetcher<List<TemplateVersion>> {

    override fun get(dataFetchingEnvironment: DataFetchingEnvironment): List<TemplateVersion> {
        return templateVersionRepository.findAll()
    }
}