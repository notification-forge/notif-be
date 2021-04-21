package com.forge.messageservice.resolvers.queries

import com.forge.messageservice.entities.inputs.PaginationInput
import com.forge.messageservice.entities.pages.ImagePages
import com.forge.messageservice.services.ImageService
import com.forge.messageservice.services.PaginationService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
class ImageResolver(
    private val imageService: ImageService,
    private val paginationService: PaginationService
) : GraphQLQueryResolver {

    fun imagePages(name: String, appCodes: List<String>, paginationInput: PaginationInput): ImagePages {
        val images = imageService.getAllImagesWithImageNameAndInAppCodes(appCodes, name, paginationInput)
        return paginationService.imagePagination(images)
    }
}