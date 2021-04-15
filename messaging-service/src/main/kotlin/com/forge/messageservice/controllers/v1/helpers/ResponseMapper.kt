package com.forge.messageservice.controllers.v1.helpers

import com.forge.messageservice.controllers.v1.api.ImageUploadResponseV1
import com.forge.messageservice.entity.Image
import org.springframework.util.Base64Utils
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

object ResponseMapper {

    fun toImageUploadResponse(image: Image): ImageUploadResponseV1 =
        ImageUploadResponseV1(
            image.id,
            image.appCode,
            image.fileName,
            base64Encode(image.imageData),
            image.createdBy,
            image.createdDate.atZone(ZoneId.systemDefault()).toEpochSecond()
        )

    private fun base64Encode(byteArray: ByteArray?): String? = if (byteArray != null) {
        Base64Utils.encodeToString(byteArray)
    } else {
        null
    }
}