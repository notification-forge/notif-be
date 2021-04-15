package com.forge.messageservice.repositories

import com.forge.messageservice.entities.Image

interface ImageRepositoryCustom {

    fun findImagesWithSimilarNames(appCode: String, filename: String): List<Image>

}