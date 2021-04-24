package com.forge.messageservice.graphql.models.inputs

data class ImageSearchFilterInput(
    val fileNamePortion: String,
    val appCodes: List<String>
)
