package com.forge.messageservice.resolvers.queries

import com.forge.messageservice.entities.Image
import com.forge.messageservice.graphql.CursorResolver.endCursor
import com.forge.messageservice.graphql.CursorResolver.from
import com.forge.messageservice.graphql.CursorResolver.startCursor
import com.forge.messageservice.graphql.GraphQLConnection
import com.forge.messageservice.graphql.extensions.Connection
import com.forge.messageservice.graphql.models.inputs.ImageSearchFilterInput
import com.forge.messageservice.graphql.models.inputs.PaginationInput
import com.forge.messageservice.services.ImageService
import graphql.kickstart.tools.GraphQLQueryResolver

import graphql.relay.DefaultEdge
import graphql.relay.DefaultPageInfo
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

@Component
class ImageResolver(
    private val imageService: ImageService
) : GraphQLQueryResolver {

    fun getImages(searchFilter: ImageSearchFilterInput, pageRequest: PaginationInput): Connection<Image> {
        val paginatedList = imageService.findImagesWhoseFilenamesMatches(
            searchFilter.appCodes,
            searchFilter.fileNamePortion,
            PageRequest.of(
                pageRequest.pageNumber, pageRequest.rowPerPage, pageRequest.sortDirection, pageRequest.sortField
            )
        )

        val edges = paginatedList.content.map { image ->
            DefaultEdge(image, from(image.id))
        }

        return GraphQLConnection(
            paginatedList.numberOfElements,
            edges,
            DefaultPageInfo(
                startCursor(edges),
                endCursor(edges),
                paginatedList.hasPrevious(),
                paginatedList.hasNext()
            )
        )

    }
}