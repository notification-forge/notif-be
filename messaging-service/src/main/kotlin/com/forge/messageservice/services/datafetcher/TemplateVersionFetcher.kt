package com.forge.messageservice.services.datafetcher

import com.forge.messageservice.entity.TemplateVersion
import com.forge.messageservice.repositories.TemplateVersionRepository
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component
class TemplateVersionDataFetcher(
    private val templateVersionRepository: TemplateVersionRepository
) : DataFetcher<TemplateVersion> {

    override fun get(dataFetchingEnvironment: DataFetchingEnvironment): TemplateVersion {
        val id = dataFetchingEnvironment.getArgument<Long>("id")
        return templateVersionRepository.getOne(id)
    }
}