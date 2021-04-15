package com.forge.messageservice.resolvers.mutations

import com.forge.messageservice.entities.Image
import com.forge.messageservice.services.ImageService
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext
import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component
class ImageMutation(
    private val imageService: ImageService
) : GraphQLMutationResolver {

    fun uploadImage(env: DataFetchingEnvironment): Image {
        val context = env.getContext<DefaultGraphQLServletContext>()
        val image = Image()
        val appCode = "BCAT"

        context.fileParts.map { it ->
            {
                image.appCode = appCode
                image.fileName = it.submittedFileName
                image.imageData = it.inputStream.toString()
            }
        }

        return imageService.save(image)
    }
}