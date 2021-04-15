package com.forge.messageservice.controllers.v1


import com.forge.messageservice.entities.Image
import com.forge.messageservice.entities.Tenant
import com.forge.messageservice.services.ImageService
import com.forge.messageservice.services.TenantService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@ExtendWith(SpringExtension::class)
@WebMvcTest(ImageHandlerController::class)
internal class ImageHandlerControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var imageService: ImageService

    @MockkBean
    lateinit var tenantService: TenantService

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

        this.mockMvc
            .perform(
                multipart("/v1/images/upload/some-tenant")
                    .file("file", mockImage.bytes)
            )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("imageId")))

    }
}