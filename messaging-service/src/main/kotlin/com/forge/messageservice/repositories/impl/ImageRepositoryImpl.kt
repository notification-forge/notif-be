package com.forge.messageservice.repositories.impl

import com.forge.messageservice.entities.Image
import com.forge.messageservice.repositories.ImageRepositoryCustom
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
open class ImageRepositoryImpl(
    private val em: EntityManager
) : ImageRepositoryCustom {

    override fun findImagesWithSimilarNames(appCode: String, filename: String): List<Image> {
        val filenameToken = filename.split(".")
        val filenameWildcard = "${filenameToken[0]}(%).${filenameToken[1]}"
        val query = em
            .createNamedQuery("Image.findWithSimilarNames")
            .setParameter("appCode", appCode)
            .setParameter("rawFilename", filename)
            .setParameter("filenameWildcard", filenameWildcard)
        return query.resultList.toList() as List<Image>
    }
}