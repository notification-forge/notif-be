package com.forge.messageservice.services

import com.forge.messageservice.common.files.SimilarFilenameGenerator
import com.forge.messageservice.entities.Image

import com.forge.messageservice.repositories.ImageRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service

open class ImageService(
    private val imageRepository: ImageRepository
) {

    @Transactional(Transactional.TxType.REQUIRED)
    open fun create(image: Image): Image {
        // check whether there is already an image having
        // similar name
        val imageHavingSimilarName = imageRepository.findImagesWithSimilarNames(
            image.appCode, image.fileName
        )

        // Just like in windows, when there is already a file having a similar name, give it an index within the filename
        // e.g. image.png becomes image(1).png

        if (imageHavingSimilarName.isNotEmpty()) {
            image.fileName = SimilarFilenameGenerator.generateFilename(image.fileName, imageHavingSimilarName)
        }

        return imageRepository.save(image)
    }

    fun getAllImages(appCode: String): List<Image> {
        return imageRepository.findAllByAppCode(appCode)
    }

    fun getAllImageIdAfterCursor(appCode: String, cursor: Long): List<Image> {
        return imageRepository.findAllByAppCodeAfterImageId(appCode, cursor)
    }
}