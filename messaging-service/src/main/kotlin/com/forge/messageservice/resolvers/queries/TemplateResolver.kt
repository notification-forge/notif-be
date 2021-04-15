package com.forge.messageservice.resolvers.queries

import com.forge.messageservice.connections.CursorUtil
import com.forge.messageservice.entities.Template
import com.forge.messageservice.entities.TemplateVersion
import com.forge.messageservice.services.TemplateService
import com.forge.messageservice.services.TemplateVersionService
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.relay.*
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class TemplateResolver(
    private val templateService: TemplateService,
    private val templateVersionService: TemplateVersionService,
    private val cursorUtil: CursorUtil
)  : GraphQLQueryResolver {

    fun template(templateId: Long): Template{
        return templateService.retrieveTemplateById(templateId)
    }

    fun templateVersion(templateVersionId: Long): TemplateVersion{
        return templateVersionService.retrieveTemplateVersionById(templateVersionId)
    }

    fun templates(searchValue: String, appCodes: List<String>, limit: Long, @Nullable cursor: Long?): Connection<Template> {

        val edges: List<Edge<Template>> = getAllTemplates(searchValue, appCodes, cursor).map { template ->
            DefaultEdge(template, cursorUtil.from(template.id!!))
        }
            .stream()
            .limit(limit)
            .collect(Collectors.toList())

        val firstCursor = cursorUtil.getFirstCursorFrom(edges)
        val lastCursor = cursorUtil.getLastCursorFrom(edges)
        val pageInfo = DefaultPageInfo(firstCursor, lastCursor, cursor != null, edges.size >= limit)

        return DefaultConnection(edges, pageInfo)
    }

    private fun getAllTemplates(name: String, appCodes: List<String>, cursor: Long?): List<Template> {
        if (cursor == null) {
            return templateService.retrieveAllTemplates(name, appCodes)
        }
        return templateService.retrieveTemplateIdAfterCursor(appCodes, cursor)
    }
}