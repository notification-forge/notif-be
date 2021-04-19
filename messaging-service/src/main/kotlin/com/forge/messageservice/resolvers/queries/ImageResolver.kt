package com.forge.messageservice.resolvers.queries

import com.forge.messageservice.connections.CursorUtil
import com.forge.messageservice.entities.Image
import com.forge.messageservice.services.ImageService
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.relay.*
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class ImageResolver(
    private val imageService: ImageService,
    private val cursorUtil: CursorUtil
)  : GraphQLQueryResolver {

    fun images(appCode: String, limit: Long, @Nullable cursor: Long?): Connection<Image> {

        val edges: List<Edge<Image>> = getAllImages(appCode, cursor).map { image ->
            DefaultEdge(image, cursorUtil.from(image.id!!))
        }
            .stream()
            .limit(limit)
            .collect(Collectors.toList())

        val firstCursor = cursorUtil.getFirstCursorFrom(edges)
        val lastCursor = cursorUtil.getLastCursorFrom(edges)
        val pageInfo = DefaultPageInfo(firstCursor, lastCursor, cursor != null, edges.size >= limit)

        return DefaultConnection(edges, pageInfo)
    }

    private fun getAllImages(appCode: String, cursor: Long?): List<Image> {
        if (cursor == null) {
            return imageService.getAllImages(appCode)
        }
        return imageService.getAllImageIdAfterCursor(appCode, cursor)
    }
}