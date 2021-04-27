package com.forge.messageservice.graphql.models

import com.forge.messageservice.common.extensions.toBase64String
import com.forge.messageservice.entities.Image
import java.time.LocalDateTime

data class GQLImage(
    val id: Long,
    val appCode: String,
    val contentType: String,
    val fileName: String,
    val fileSignature: String,
    val status: Image.ImageStatus,
    val imageData: String,
    val createdDate: LocalDateTime?,
    val createdBy: String?,
    val lastModifiedDate: LocalDateTime?,
    val lastModifiedBy: String?
) {

    companion object {
        fun from(image: Image): GQLImage = GQLImage(
            image.id,
            image.appCode,
            image.contentType,
            image.fileName,
            image.fileSignature,
            image.status,
            image.imageData?.toBase64String() ?: "0x00",
            image.createdDate,
            image.createdBy,
            image.lastModifiedDate,
            image.lastModifiedBy
        )
    }

}
