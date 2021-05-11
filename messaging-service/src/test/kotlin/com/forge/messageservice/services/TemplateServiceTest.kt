package com.forge.messageservice.services

import com.forge.messageservice.entities.Template
import com.forge.messageservice.entities.Template.AlertType
import com.forge.messageservice.exceptions.GraphQLQueryException
import com.forge.messageservice.exceptions.TemplateDoesNotExistException
import com.forge.messageservice.exceptions.TemplateExistedException
import com.forge.messageservice.graphql.models.inputs.CreateTemplateInput
import com.forge.messageservice.graphql.models.inputs.PaginationInput
import com.forge.messageservice.graphql.models.inputs.UpdateTemplateInput
import com.forge.messageservice.repositories.TemplateRepository
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
import java.util.*

@ExtendWith(MockKExtension::class)
class TemplateServiceTest {

    @MockK
    lateinit var templateRepository: TemplateRepository

    lateinit var templateService: TemplateService

    @BeforeEach
    fun setUp() {
        templateService = TemplateService(templateRepository)
    }

    @AfterEach
    fun tearDown() = unmockkAll()

    private fun mockTemplateOne(): Template {
        return Template().apply {
            id = 1L
            name = "Apology Template One"
            appCode = "AppOne"
            alertType = AlertType.EMAIL
        }
    }

    private fun mockTemplateTwo(): Template {
        return Template().apply {
            id = 2L
            name = "Apology Template Two"
            appCode = "AppTwo"
            alertType = AlertType.EMAIL
        }
    }

    private fun mockTemplateThree(): Template {
        return Template().apply {
            id = 3L
            name = "Apology Template Three"
            appCode = "AppOne"
            alertType = AlertType.EMAIL
        }
    }

    private fun mockListOfTemplates() = PageImpl(listOf(mockTemplateTwo(), mockTemplateThree()))

    @Test
    fun itShouldReturnTemplatesSimilarNameAndWithinAppCodes() {
        val searchValue = "Apology"
        val appCodes = listOf("AppOne", "AppTwo")
        val paginationInput = PaginationInput(1, 10, Sort.Direction.ASC, "id")
        val pageable = PageRequest.of(
            paginationInput.pageNumber,
            paginationInput.rowPerPage,
            paginationInput.sortDirection,
            paginationInput.sortField
        )

        every {
            templateRepository.findWithNamesLike(
                appCodes,
                searchValue,
                pageable
            )
        } returns mockListOfTemplates()

        val templates = templateService.getAllTemplatesWithTemplateNameAndInAppCodes(
            appCodes,
            searchValue,
            pageable,
            paginationInput.sortField!!
        )

        templates.forEach { template ->
            run {
                assert(template.appCode in appCodes)
                assert(template.name!!.contains(searchValue))
            }
        }
    }

    @Test
    fun itShouldReturnTemplateWhenTemplateIdExistAndExceptionWhenItDoesNot() {
        val templateIdExist = 1L
        val templateIdDoesNotExist = 0L

        every { templateRepository.findById(templateIdExist) } returns Optional.of(mockTemplateOne())
        every { templateRepository.findById(templateIdDoesNotExist) } returns Optional.empty()

        val template = templateService.getTemplateById(templateIdExist)

        assert(template.id == templateIdExist)
        assertThrows<TemplateDoesNotExistException> { templateService.getTemplateById(templateIdDoesNotExist) }
    }

    @Test
    fun itShouldReturnTemplateWhenCreatingTemplate() {
        val templateName = "Apology Template Four"
        val templateAppCode = "AppOne"
        val templateAlertType = AlertType.EMAIL

        val createTemplateInput = CreateTemplateInput(templateName, templateAlertType, templateAppCode)

        val mockTemplate = Template().apply {
            name = templateName
            alertType = templateAlertType
            appCode = templateAppCode
        }

        every { templateRepository.findByNameAndAppCode(templateName, templateAppCode) } returns null
        every { templateRepository.save(any()) } returns mockTemplate

        val template = templateService.createTemplate(createTemplateInput)

        assert(template.name == templateName)
        assert(template.alertType == templateAlertType)
        assert(template.appCode == templateAppCode)

    }

    @Test
    fun itShouldThrowAnExceptionWhenCreatingTemplateWhereTemplateNameAndAppCodeAlreadyExist() {
        val templateNameExisted = "Apology Template One"
        val templateAppCode = "AppOne"
        val templateAlertType = AlertType.EMAIL

        val createTemplateExistedInput = CreateTemplateInput(templateNameExisted, templateAlertType, templateAppCode)
        every {
            templateRepository.findByNameAndAppCode(
                templateNameExisted,
                templateAppCode
            )
        } returns mockTemplateOne()

        assertThrows<TemplateExistedException> { templateService.createTemplate(createTemplateExistedInput) }

    }

    @Test
    fun itShouldReturnTemplateWhenUpdatingTemplate() {
        val templateId = 1L
        val templateName = "Apology Template Four"
        val templateAppCode = "AppOne"
        val templateAlertType = AlertType.EMAIL

        val updateTemplateInput = UpdateTemplateInput(templateId, templateName)

        val mockTemplate = Template().apply {
            id = templateId
            name = templateName
            appCode = templateAppCode
            alertType = templateAlertType
        }

        every { templateRepository.findById(templateId) } returns Optional.of(mockTemplateOne())
        every { templateRepository.findByNameAndAppCode(templateName, templateAppCode) } returns null
        every { templateRepository.save(any()) } returns mockTemplate


        val template = templateService.updateTemplate(updateTemplateInput)
        assert(template.id == templateId)
        assert(template.name == templateName)
        assert(template.alertType == templateAlertType)
        assert(template.appCode == templateAppCode)

    }


    @Test
    fun itShouldThrowAnExceptionWhenUpdatingAnTemplateWhereTemplateIdDoesNotExist() {
        val templateIdDoesNotExist = 0L
        val templateName = "Apology Template One"

        val updateTemplateIdDoesNotExistInput = UpdateTemplateInput(templateIdDoesNotExist, templateName)
        every { templateRepository.findById(templateIdDoesNotExist) } returns Optional.empty()

        assertThrows<TemplateDoesNotExistException> { templateService.updateTemplate(updateTemplateIdDoesNotExistInput) }

    }

    @Test
    fun itShouldThrowAnExceptionWhenUpdatingTemplateWhereTemplateNameAndAppCodeAlreadyExist() {
        val templateId = 1L
        val templateNameExisted = "Apology Template One"
        val templateAppCode = "AppOne"

        val updateTemplateExistedInput = UpdateTemplateInput(templateId, templateNameExisted)

        every { templateRepository.findById(templateId) } returns Optional.of(mockTemplateOne())
        every {
            templateRepository.findByNameAndAppCode(
                templateNameExisted,
                templateAppCode
            )
        } returns mockTemplateOne()

        assertThrows<TemplateExistedException> { templateService.updateTemplate(updateTemplateExistedInput) }
    }

    @Test
    fun itShouldThrowExceptionWhenReturnPageTemplateAndSortFieldInputInvalid() {
        val searchValue = "Apology"
        val appCodes = listOf("AppOne", "AppTwo")
        val paginationInput = PaginationInput(1, 10, Sort.Direction.ASC, "names")
        val pageable = PageRequest.of(
            paginationInput.pageNumber,
            paginationInput.rowPerPage,
            paginationInput.sortDirection,
            paginationInput.sortField
        )

        every {
            templateRepository.findWithNamesLike(
                appCodes,
                searchValue,
                pageable
            )
        } throws Exception()


        assertThrows<GraphQLQueryException> {
            templateService.getAllTemplatesWithTemplateNameAndInAppCodes(
                appCodes,
                searchValue,
                pageable,
                paginationInput.sortField!!
            )
        }
    }

    @Test
    fun itShouldReturnPageTemplateAndSortFieldInputValid() {
        val searchValue = "Apology"
        val appCodes = listOf("AppOne", "AppTwo")
        val paginationInput = PaginationInput(1, 10, Sort.Direction.ASC, "names")
        val pageable = PageRequest.of(
            paginationInput.pageNumber,
            paginationInput.rowPerPage,
            paginationInput.sortDirection,
            paginationInput.sortField
        )

        every {
            templateRepository.findWithNamesLike(
                appCodes,
                searchValue,
                pageable
            )
        } returns mockListOfTemplates()

        assert(!mockListOfTemplates().isEmpty)
        assert(pageable.isPaged)
    }
}