package com.forge.messageservice.services

import com.forge.messageservice.entities.Template
import com.forge.messageservice.entities.TemplateVersion
import com.forge.messageservice.entities.TemplateVersion.TemplateStatus.DRAFT
import com.forge.messageservice.entities.TemplateVersion.TemplateStatus.PUBLISHED
import com.forge.messageservice.entities.inputs.CreateTemplateVersionInput
import com.forge.messageservice.entities.inputs.UpdateTemplateVersionInput
import com.forge.messageservice.exceptions.TemplateVersionDoesNotExistException
import com.forge.messageservice.repositories.TemplateRepository
import com.forge.messageservice.repositories.TemplateVersionRepository
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
class TemplateVersionServiceTest {

    @MockK
    lateinit var templateRepository: TemplateRepository

    @MockK
    lateinit var templateVersionRepository: TemplateVersionRepository

    lateinit var templateVersionService: TemplateVersionService

    @BeforeEach
    fun setUp() {
        templateVersionService = TemplateVersionService(templateVersionRepository, templateRepository)
    }

    @AfterEach
    fun tearDown() = unmockkAll()

    private fun mockTemplate(): Template {
        return Template().apply {
            id = 1L
            name = "Apology Template One"
            appCode = "AppOne"
            alertType = Template.AlertType.EMAIL
        }
    }

    private fun mockTemplateVersionOnePublished(): TemplateVersion {
        return TemplateVersion().apply {
            id = 1L
            name = "template version name 1"
            templateId = 1L
            settings = "{ \"to\": [\"receipient@company.com\"], " +
                    "\"cc\": [\"receipient@company.com\"], " +
                    "\"bcc\": [\"receipient@company.com\"], " +
                    "\"subject\": \"Bill Alert for the month of {{ month }} {{ year }}\", " +
                    "\"sender\": [\"{{ app.emails.defaultSender }}\"], " +
                    " \"format\": \"html | rich text\" }"
            templateHash = 1234567
            body = "Hi, Bill Alert for the month of {{ month }} {{ year }}"
            version = 1L
            status = PUBLISHED
        }
    }

    private fun mockTemplateVersionOneDraft(): TemplateVersion {
        return TemplateVersion().apply {
            id = 2L
            name = "template version name 2"
            templateId = 1L
            settings = "{ \"to\": [\"receipient@company.com\"], " +
                    "\"cc\": [\"receipient@company.com\"], " +
                    "\"bcc\": [\"receipient@company.com\"], " +
                    "\"subject\": \"Bill Alert for the month of {{ month }} {{ year }}\", " +
                    "\"sender\": [\"{{ app.emails.defaultSender }}\"], " +
                    " \"format\": \"html | rich text\" }"
            templateHash = 2345678
            body = "Hi, Bill Alert for the month of {{ month }} {{ year }}"
            version = 0L
            status = DRAFT
        }
    }

    private fun mockTemplateVersionTwoDraft(): TemplateVersion {
        return TemplateVersion().apply {
            id = 2L
            name = "template version name 2"
            templateId = 1L
            settings = "{ \"to\": [\"receipient@company.com\"], " +
                    "\"cc\": [\"receipient@company.com\"], " +
                    "\"bcc\": [\"receipient@company.com\"], " +
                    "\"subject\": \"Bill Alert for the month of {{ month }} {{ year }}\", " +
                    "\"sender\": [\"{{ app.emails.defaultSender }}\"], " +
                    " \"format\": \"html | rich text\" }"
            templateHash = 2345678
            body = "Hi, Bill Alert for the month of {{ month }} {{ year }}"
            version = 0L
            status = DRAFT
        }
    }

    private fun mockListOfTemplateVersions() = listOf(mockTemplateVersionOnePublished(), mockTemplateVersionTwoDraft())

    @Test
    fun itShouldReturnTemplateVersionsWhenTemplateIdExistAndEmptyListWhenTemplateIdDoesNotExist() {
        val templateId = 1L
        val templateIdThatDoesNotExist = 0L

        every { templateVersionRepository.findAllByTemplateId(templateId) } returns mockListOfTemplateVersions()
        every { templateVersionRepository.findAllByTemplateId(templateIdThatDoesNotExist) } returns listOf()

        val templateVersions = templateVersionService.getAllTemplateVersionsByTemplateId(templateId)
        val blankTemplateVersions =
            templateVersionService.getAllTemplateVersionsByTemplateId(templateIdThatDoesNotExist)

        assert(templateVersions.isNotEmpty())
        templateVersions.forEach { templateVersion ->
            run {
                assert(templateVersion.templateId == templateId)
            }
        }
        assert(blankTemplateVersions.isNullOrEmpty())
    }

    @Test
    fun itShouldReturnTemplateVersionWhenTemplateVersionIdExistAndThrowsExceptionWhenTemplateVersionIdDoesNotExist() {
        val templateVersionIdExist = 1L
        val templateVersionIdDoesNotExist = 0L

        every { templateVersionRepository.findById(templateVersionIdExist) } returns Optional.of(
            mockTemplateVersionOnePublished()
        )
        every { templateVersionRepository.findById(templateVersionIdDoesNotExist) } returns Optional.empty()

        val templateVersion = templateVersionService.getTemplateVersionById(templateVersionIdExist)

        assert(templateVersion.id == templateVersionIdExist)
        assertThrows<TemplateVersionDoesNotExistException> {
            templateVersionService.getTemplateVersionById(
                templateVersionIdDoesNotExist
            )
        }
    }

    @Test
    fun itShouldReturnTemplateWhenCreatingTemplateVersionAndExceptionWhenTemplateDoesNotExist() {
        val templateIdExist = 1L
        val templateIdDoesNotExist = 0L

        val createTemplateVersionInput = CreateTemplateVersionInput(templateIdExist)
        val createTemplateVersionInputWithTemplateIdDoesNotExist = CreateTemplateVersionInput(templateIdDoesNotExist)

        val mockTemplateVersion = TemplateVersion().apply {
            templateId = templateIdExist
            status = DRAFT
            templateHash = 0
        }

        every {
            templateVersionRepository.findByTemplateIdAndStatus(
                templateIdExist,
                DRAFT
            )
        } returns mockTemplateVersionTwoDraft()
        every { templateVersionRepository.findByTemplateIdAndStatus(templateIdDoesNotExist, DRAFT) } returns null
        every { templateRepository.findById(templateIdExist) } returns Optional.of(mockTemplate())
        every { templateRepository.findById(templateIdDoesNotExist) } returns Optional.empty()
        every { templateVersionRepository.save(mockTemplateVersion) } returns mockTemplateVersion

        val templateVersion = templateVersionService.createTemplateVersion(createTemplateVersionInput)

        assert(templateVersion.id == templateIdExist)
        assert(templateVersion.status == DRAFT)
        assert(templateVersion.templateHash == 0)

        assertThrows<TemplateVersionDoesNotExistException> {
            templateVersionService.createTemplateVersion(
                createTemplateVersionInputWithTemplateIdDoesNotExist
            )
        }
    }

    @Test
    fun itShouldReturnTemplateVersionWhenUpdatingTemplateVersionAndThrowsExceptionWhenTemplateVersionIdDoesNotExist() {
        val templateVersionId = 1L
        val templateVersionIdDoesNotExist = 0L
        val templateVersionName = "New Template Version Name"
        val templateVersionSettings = "{ \"to\": [\"receipient@company.com\"] }"
        val templateVersionBody = "Hi {{ username }}, this email is to inform you about your bill alert."

        val missingTemplateVersionIdInput = UpdateTemplateVersionInput(
            templateVersionIdDoesNotExist, templateVersionName, templateVersionSettings, templateVersionBody, DRAFT
        )
        val updatePublishTemplateVersionInput = UpdateTemplateVersionInput(
            templateVersionId, templateVersionName, templateVersionSettings, templateVersionBody, PUBLISHED
        )

        val mockPublishedTemplateVersion = TemplateVersion().apply {
            id = templateVersionId
            name = templateVersionName
            settings = templateVersionSettings
            body = templateVersionBody
            version = 3L
            status = PUBLISHED
        }
        mockPublishedTemplateVersion.templateHash = mockPublishedTemplateVersion.templateHash()

        every { templateVersionRepository.findById(templateVersionId) } returns Optional.of(mockTemplateVersionOneDraft())
        every { templateVersionRepository.findById(templateVersionIdDoesNotExist) } returns Optional.empty()
        every { templateVersionRepository.save(any()) } returns mockPublishedTemplateVersion
        every { templateVersionRepository.findCurrentVersionNumberByTemplateId(1L) } returns 3L

        val publishTemplateVersion = templateVersionService.updateTemplateVersion(updatePublishTemplateVersionInput)

        assert(publishTemplateVersion.id == templateVersionId)
        assert(publishTemplateVersion.status == PUBLISHED)

        assertThrows<TemplateVersionDoesNotExistException> {
            templateVersionService.updateTemplateVersion(
                missingTemplateVersionIdInput
            )
        }
    }

}