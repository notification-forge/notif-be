package com.forge.messageservice.resolvers.mutations

import com.forge.messageservice.entities.Image
import com.forge.messageservice.services.ImageService
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext
import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream
import javax.servlet.http.Part

@Component
class ImageMutation(
    private val imageService: ImageService
) : GraphQLMutationResolver {

    fun uploadImage(image: Part, env: DataFetchingEnvironment): Image {
        val context = env.getContext<DefaultGraphQLServletContext>()
        val appCode = "BCAT"

        val image = context.fileParts.mapNotNull { parts ->
            parts.inputStream.use { `is` ->
                ByteArrayOutputStream().use { os ->
                    os.writeBytes(`is`.readAllBytes())
                    Image().apply {
                        this.appCode = appCode
                        this.fileName = parts.submittedFileName
                        this.imageData = os.toByteArray()
                    }
                }
            }
        }

        return imageService.create(image.first())
    }
}