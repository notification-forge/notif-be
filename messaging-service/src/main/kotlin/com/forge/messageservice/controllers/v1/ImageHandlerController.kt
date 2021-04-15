package com.forge.messageservice.controllers.v1

import com.forge.messageservice.controllers.v1.api.ImageUploadResponseV1
import com.forge.messageservice.controllers.v1.helpers.ResponseMapper
import com.forge.messageservice.entities.Image
import com.forge.messageservice.exceptions.TenantNotFoundException

import com.forge.messageservice.services.ImageService
import com.forge.messageservice.services.TenantService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

/**
 * Applications can upload images for use in their notification. Images are stored as blob in the database (s3 next phase).
 */
@RestController
@RequestMapping("/v1/images")
class ImageHandlerController(
    private val imageService: ImageService,
    private val tenantService: TenantService
) {

    /**
     * Handles uploaded images.
     */
    @PostMapping("/upload/{appCode}")
    fun handleUpload(
        @PathVariable appCode: String,
        @RequestPart file: MultipartFile
    ): ResponseEntity<ImageUploadResponseV1> {

        val tenant = tenantService.findTenant(appCode)

        if (tenant != null) {
            val response = imageService.create(Image().apply {
                this.appCode = tenant.appCode
                this.fileName = file.originalFilename!!
                this.contentType = file.contentType
                this.imageData = file.bytes
            })

            return ResponseEntity.ok(ResponseMapper.toImageUploadResponse(response))
        }

        throw TenantNotFoundException("No tenant found identified by $appCode")
    }

}