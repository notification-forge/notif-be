package com.forge.messageservice.services

import com.forge.messageservice.common.files.SimilarFilenameGenerator
import com.forge.messageservice.entities.Image
import com.forge.messageservice.repositories.ImageRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class ImageService(
    private val imageRepository: ImageRepository
) {

    @Transactional(Transactional.TxType.REQUIRED)
    fun create(image: Image): Image {
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

    /**
     * Returns images in the requested `appCodes` whose filenames matches a portion of the requested `fileNamePortion`
     */
    @Transactional(Transactional.TxType.NEVER)
    fun findImagesWhoseFilenamesMatches(
        appCodes: List<String>,
        fileNamePortion: String,
        pageable: Pageable
    ): Page<Image> {
        return imageRepository.findWithNamesLike(appCodes, fileNamePortion, pageable)
    }
}