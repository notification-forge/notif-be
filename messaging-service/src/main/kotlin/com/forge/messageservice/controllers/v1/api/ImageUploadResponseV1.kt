package com.forge.messageservice.controllers.v1.api

data class ImageUploadResponseV1(
    val imageId: Long,
    val appCode: String,
    val fileName: String,
    val imageData: String?,
    val createdBy: String,
    val dateCreated: Long
)
