package com.forge.messageservice.services

import com.forge.messageservice.entities.Image
import com.forge.messageservice.repositories.ImageRepository
import org.springframework.stereotype.Service

@Service
open class ImageService(private val imageRepository: ImageRepository) {

    fun retrieveAllImages(appCode: String): List<Image> {
        return imageRepository.findAllByAppCode(appCode)
    }

    fun retrieveImageIdAfterCursor(appCode: String, cursor: Long): List<Image> {
        return imageRepository.findAllByAppCodeAfterImageId(appCode, cursor)
    }

    fun save(image: Image): Image {
        return imageRepository.save(image)
    }
}