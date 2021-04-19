package com.forge.messageservice.resolvers.mutations

import com.forge.messageservice.entities.Image
import com.forge.messageservice.services.ImageService
import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream
import javax.servlet.http.Part

@Component
class ImageMutation(
    private val imageService: ImageService
) : GraphQLMutationResolver {

    fun uploadImage(appCode: String, input: Part, env: DataFetchingEnvironment): Image {

        val image = input.inputStream.use { `is` ->
            ByteArrayOutputStream().use { os ->
                os.writeBytes(`is`.readAllBytes())
                Image().apply {
                    this.appCode = appCode
                    this.fileName = input.submittedFileName
                    this.imageData = os.toByteArray()
                    this.contentType = input.contentType
                }
            }
        }

        return imageService.create(image)
    }
}