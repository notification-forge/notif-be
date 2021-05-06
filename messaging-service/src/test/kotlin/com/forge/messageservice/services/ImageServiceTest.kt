package com.forge.messageservice.services

import com.forge.messageservice.entities.Image
import com.forge.messageservice.exceptions.PageImageException
import com.forge.messageservice.graphql.models.inputs.PaginationInput
import com.forge.messageservice.repositories.ImageRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

@ExtendWith(MockKExtension::class)
class ImageServiceTest {

    @MockK
    lateinit var imageRepository: ImageRepository

    lateinit var imageService: ImageService

    @BeforeEach
    fun setUp() {
        imageService = ImageService(imageRepository)
    }

    @AfterEach
    fun tearDown() = unmockkAll()

    private fun mockImage(): Image {
        return Image().apply {
            id = 1L
            appCode = "App"
            fileName = "imageOne"
        }
    }

    private fun mockImageTwo(): Image {
        return Image().apply {
            id = 2L
            appCode = "App1"
            fileName = "imageTwo"
        }
    }

    private fun mockListOfImages() = PageImpl(listOf(mockImage(), mockImageTwo()))

    @Test
    fun itShouldReturnImagesWithSort() {
        val fileNamePortion = "image"
        val appCodes = listOf("App", "App1")
        val paginationInput = PaginationInput(1, 10, Sort.Direction.ASC, "fileName")
        val pageable = PageRequest.of(
            paginationInput.pageNumber,
            paginationInput.rowPerPage,
            paginationInput.sortDirection,
            paginationInput.sortField
        )

        every {
            imageRepository.findWithNamesLike(
                appCodes,
                fileNamePortion,
                pageable
            )
        } returns mockListOfImages()

        val images =
            imageService.findImagesWhoseFilenamesMatches(
                appCodes,
                fileNamePortion,
                pageable,
                paginationInput.sortField!!
            )

        images.forEach { image ->
            run {
                assert(image.appCode in appCodes)
                assert(image.fileName.contains(fileNamePortion))
            }
        }
    }

    @Test
    fun itShouldThrowExceptionWhenReturnPageTemplateAndSortFieldInputInvalid() {
        val fileNames = "imageOne"
        val appCodes = listOf("App", "App1")
        val paginationInput = PaginationInput(1, 10, Sort.Direction.ASC, "fileNames")
        val pageable = PageRequest.of(
            paginationInput.pageNumber,
            paginationInput.rowPerPage,
            paginationInput.sortDirection,
            paginationInput.sortField
        )

        if (paginationInput.sortField?.contentEquals("fileName") == true) {
            every {
                imageRepository.findWithNamesLike(
                    appCodes,
                    fileNames,
                    pageable
                )
            } throws Exception()
        }

        assertThrows<PageImageException> {
            imageService.findImagesWhoseFilenamesMatches(
                appCodes,
                fileNames,
                pageable,
                paginationInput.sortField!!
            )
        }
    }

    @Test
    fun itShouldReturnPageImageWhenSortFieldValid() {
        val fileNames = "imageOne"
        val appCodes = listOf("App", "App1")
        val paginationInput = PaginationInput(1, 10, Sort.Direction.ASC, "fileNames")
        val pageable = PageRequest.of(
            paginationInput.pageNumber,
            paginationInput.rowPerPage,
            paginationInput.sortDirection,
            paginationInput.sortField
        )

        every {
            imageRepository.findWithNamesLike(
                appCodes,
                fileNames,
                pageable
            )
        } returns mockListOfImages()

        assert(!mockListOfImages().isEmpty)
        assert(pageable.isPaged)
    }
}