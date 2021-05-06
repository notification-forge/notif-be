package com.forge.messageservice.resolvers.queries

import com.forge.messageservice.graphql.CursorResolver.from
import com.forge.messageservice.graphql.extensions.Connection
import com.forge.messageservice.graphql.models.GQLImage
import com.forge.messageservice.graphql.models.inputs.ImageSearchFilterInput
import com.forge.messageservice.graphql.models.inputs.PaginationInput
import com.forge.messageservice.resolvers.queries.helpers.GQLConnectionHelper.gqlConnectionFor
import com.forge.messageservice.services.ImageService
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.relay.DefaultEdge
import org.springframework.stereotype.Component

@Component
class ImageResolver(
    private val imageService: ImageService
) : GraphQLQueryResolver {

    fun getImages(searchFilter: ImageSearchFilterInput, pageRequestInput: PaginationInput): Connection<GQLImage> {
        if (pageRequestInput.sortField.isNullOrEmpty()) {
            pageRequestInput.sortField = "fileName"
        }
        return gqlConnectionFor({
            imageService.findImagesWhoseFilenamesMatches(
                searchFilter.appCodes,
                searchFilter.fileNamePortion,
                pageRequestInput.asPageRequest(),
                pageRequestInput.sortField!!
            )
        }) {
            DefaultEdge(GQLImage.from(it), from(it.id))
        }
    }
}