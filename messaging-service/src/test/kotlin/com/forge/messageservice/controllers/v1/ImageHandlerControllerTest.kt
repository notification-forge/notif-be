package com.forge.messageservice.controllers.v1


import com.forge.messageservice.entities.Image
import com.forge.messageservice.entities.Tenant
import com.forge.messageservice.services.ImageService
import com.forge.messageservice.services.TenantService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class ImageHandlerControllerTest {

    @MockK
    lateinit var imageService: ImageService

    @MockK
    lateinit var tenantService: TenantService

    @InjectMockKs
    lateinit var imageHandlerController: ImageHandlerController

    @Test
    fun shouldReturnNewlyUploadedImageData() {
        every { tenantService.findTenant(any()) } returns Tenant().apply {
            appCode = "some-tenant"
        }

        every { imageService.create(any()) } returns Image().apply {
            id = 1L
            appCode = "some-tenant"
            fileName = "image.png"
            contentType = "image/png"
        }

        val mockImage = MockMultipartFile(
            "data",
            "filename.png",
            "image/png",
            byteArrayOf()
        )
        val resp = imageHandlerController.handleUpload("some-tenant", mockImage)
        assertEquals(HttpStatus.OK, resp.statusCode)
        assertEquals(1L, resp.body!!.imageId)

    }
}