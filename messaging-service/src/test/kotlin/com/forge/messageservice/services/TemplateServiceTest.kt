package com.forge.messageservice.services

import com.forge.messageservice.entities.Template
import com.forge.messageservice.entities.Template.AlertType
import com.forge.messageservice.entities.inputs.CreateTemplateInput
import com.forge.messageservice.entities.inputs.UpdateTemplateInput
import com.forge.messageservice.entities.pages.Page
import com.forge.messageservice.exceptions.TemplateDoesNotExistException
import com.forge.messageservice.exceptions.TemplateExistedException
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

//    private fun mockListOfTemplates() =  Page().
//    private fun mockListOfTemplatesAfterIdOne() = listOf<Template>(mockTemplateTwo(), mockTemplateThree())
//
//    @Test
//    fun itShouldReturnSimiliarNameAndWithinAppCodes() {
//        val searchValue = "Apology"
//        val appCodes = listOf("AppOne", "AppTwo")
//
//        every { templateRepository.findAllLikeNameAndInAppCodes(searchValue, appCodes) } returns mockListOfTemplates()
//
//        val templates = templateService.getAllTemplatesWithTemplateNameAndInAppCodes(searchValue, appCodes)
//
//        templates.forEach { template ->
//            run {
//                assert(template.appCode in appCodes)
//                assert(template.name!!.contains(searchValue))
//            }
//        }
//    }
//
//    @Test
//    fun itShouldReturnTemplateWhenTemplateIdExistAndExceptionWhenItDoesNot() {
//        val templateIdExist = 1L
//        val templateIdDoesNotExist = 0L
//
//        every { templateRepository.findById(templateIdExist) } returns Optional.of(mockTemplateOne())
//        every { templateRepository.findById(templateIdDoesNotExist) } returns Optional.empty()
//
//        val template = templateService.getTemplateById(templateIdExist)
//
//        assert(template.id == templateIdExist)
//        assertThrows<TemplateDoesNotExistException> { templateService.getTemplateById(templateIdDoesNotExist) }
//    }
//
////    @Test
////    fun itShouldReturnTemplatesWithIdAfterCursor() {
////        val cursor = 1L
////        val appCodes = listOf("AppOne", "AppTwo")
////
////        every { templateRepository.findAllInAppCodesAfterTemplateId(appCodes, cursor) } returns mockListOfTemplatesAfterIdOne()
////
////        val templates = templateService.getTemplatesInAppCodes(appCodes, cursor)
////
////        templates.forEach { template ->
////            run {
////                assert(template.appCode in appCodes)
////                assert(template.id!! > cursor)
////            }
////        }
////    }
//
//    @Test
//    fun itShouldReturnTemplateWhenCreatingTemplateAndExceptionWhenTemplateNameAndAppCodeAlreadyExist() {
//        val templateName = "Apology Template Four"
//        val templateNameExisted = "Apology Template One"
//        val templateAppCode = "AppOne"
//        val templateAlertType = AlertType.EMAIL
//
//        val createTemplateInput = CreateTemplateInput(templateName, templateAlertType, templateAppCode)
//        val createTemplateExistedInput = CreateTemplateInput(templateNameExisted, templateAlertType, templateAppCode)
//
//        val mockTemplate = Template().apply {
//            name = templateName
//            alertType = templateAlertType
//            appCode = templateAppCode
//        }
//
//        every { templateRepository.findByNameAndAppCode(templateName, templateAppCode) } returns null
//        every { templateRepository.findByNameAndAppCode(templateNameExisted, templateAppCode) } returns mockTemplateOne()
//        every { templateRepository.save(any()) } returns mockTemplate
//
//        val template = templateService.createTemplate(createTemplateInput)
//
//        assert(template.name == templateName)
//        assert(template.alertType == templateAlertType)
//        assert(template.appCode == templateAppCode)
//
//        assertThrows<TemplateExistedException> { templateService.createTemplate(createTemplateExistedInput) }
//    }
//
//    @Test
//    fun itShouldReturnTemplateWhenUpdatingTemplateAndExceptionWhenTemplateNameAndAppCodeAlreadyExist() {
//        val templateId = 1L
//        val templateIdDoesNotExist = 0L
//        val templateName = "Apology Template Four"
//        val templateNameExisted = "Apology Template One"
//        val templateAppCode = "AppOne"
//        val templateAlertType = AlertType.EMAIL
//
//        val updateTemplateInput = UpdateTemplateInput(templateId, templateName)
//        val updateTemplateIdDoesNotExistInput = UpdateTemplateInput(templateIdDoesNotExist, templateNameExisted)
//        val updateTemplateExistedInput = UpdateTemplateInput(templateId, templateNameExisted)
//
//        val mockTemplate = Template().apply {
//            id = templateId
//            name = templateName
//            appCode = templateAppCode
//            alertType = templateAlertType
//        }
//
//        every { templateRepository.findById(templateId) } returns Optional.of(mockTemplateOne())
//        every { templateRepository.findById(templateIdDoesNotExist) } returns Optional.empty()
//        every { templateRepository.findByNameAndAppCode(templateName, templateAppCode) } returns null
//        every { templateRepository.findByNameAndAppCode(templateNameExisted, templateAppCode) } returns mockTemplateOne()
//        every { templateRepository.save(any()) } returns mockTemplate
//
//
//        val template = templateService.updateTemplate(updateTemplateInput)
//        assert(template.id == templateId)
//        assert(template.name == templateName)
//        assert(template.alertType == templateAlertType)
//        assert(template.appCode == templateAppCode)
//
//        assertThrows<TemplateDoesNotExistException> { templateService.updateTemplate(updateTemplateIdDoesNotExistInput) }
//        assertThrows<TemplateExistedException> { templateService.updateTemplate(updateTemplateExistedInput) }
//    }
}