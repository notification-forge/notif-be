package com.forge.messageservice.services

import com.alphamail.plugin.api.MessageType
import com.fasterxml.jackson.databind.ObjectMapper
import com.forge.messageservice.entities.Template
import com.forge.messageservice.entities.TemplatePlugin
import com.forge.messageservice.entities.TemplateVersion
import com.forge.messageservice.entities.TemplateVersion.TemplateStatus.DRAFT
import com.forge.messageservice.entities.TemplateVersion.TemplateStatus.PUBLISHED
import com.forge.messageservice.exceptions.TemplateVersionDoesNotExistException
import com.forge.messageservice.graphql.models.inputs.ConfigurationInput
import com.forge.messageservice.graphql.models.inputs.CreateTemplateVersionInput
import com.forge.messageservice.graphql.models.inputs.PluginInput
import com.forge.messageservice.graphql.models.inputs.UpdateTemplateVersionInput
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
    lateinit var templatePluginService: TemplatePluginService

    @MockK
    lateinit var templateRepository: TemplateRepository

    @MockK
    lateinit var templateVersionRepository: TemplateVersionRepository

    @MockK
    lateinit var objectMapper: ObjectMapper

    lateinit var templateVersionService: TemplateVersionService

    @BeforeEach
    fun setUp() {
        templateVersionService =
            TemplateVersionService(templatePluginService, templateVersionRepository, templateRepository, objectMapper)
    }

    @AfterEach
    fun tearDown() = unmockkAll()

    private fun mockTemplate(): Template {
        return Template().apply {
            id = 1L
            name = "Apology Template One"
            appCode = "AppOne"
            type = MessageType.EMAIL
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
            body = "Hi, Bill Alert for the month of {{ month }} {{ year }}"
            version = 0L
            status = DRAFT
        }
    }

    private fun mockPluginsInput(): List<PluginInput> {
        return listOf(
            PluginInput(
                1, listOf(
                    ConfigurationInput(
                        "username",
                        "Tommy"
                    ),
                    ConfigurationInput(
                        "password",
                        "abcde12345"
                    )
                )
            )
        )
    }

    private fun mockTemplatePlugins(): List<TemplatePlugin> {
        return listOf(TemplatePlugin().apply {
            id = 1L
            templateVersionId = 1L
            pluginId = 1L
            configuration = "{\\\"username\\\":\\\"Tommy\\\", \\\"password\\\":\\\"abcde12345\\\"}"
        })
    }

    private fun mockListOfTemplateVersions() = listOf(mockTemplateVersionOnePublished(), mockTemplateVersionTwoDraft())

    @Test
    fun itShouldReturnTemplateVersionsWhenTemplateIdExist() {
        val templateId = 1L

        every { templateVersionRepository.findAllByTemplateId(templateId) } returns mockListOfTemplateVersions()

        val templateVersions = templateVersionService.getAllTemplateVersionsOf(templateId)

        assert(templateVersions.isNotEmpty())
        templateVersions.forEach { templateVersion ->
            run {
                assert(templateVersion.templateId == templateId)
            }
        }
    }


    @Test
    fun itShouldReturnEmptyListWhenTemplateIdDoesNotExist() {
        val templateIdThatDoesNotExist = 0L

        every { templateVersionRepository.findAllByTemplateId(templateIdThatDoesNotExist) } returns listOf()

        val blankTemplateVersions =
            templateVersionService.getAllTemplateVersionsOf(templateIdThatDoesNotExist)

        assert(blankTemplateVersions.isNullOrEmpty())
    }

    @Test
    fun itShouldReturnTemplateVersionWhenTemplateVersionIdExist() {
        val templateVersionIdExist = 1L

        every { templateVersionRepository.findById(templateVersionIdExist) } returns Optional.of(
            mockTemplateVersionOnePublished()
        )

        val templateVersion = templateVersionService.getTemplateVersion(templateVersionIdExist)

        assert(templateVersion.id == templateVersionIdExist)
    }

    @Test
    fun itShouldThrowAnExceptionWhenTemplateVersionIdDoesNotExist() {
        val templateVersionIdDoesNotExist = 0L

        every { templateVersionRepository.findById(templateVersionIdDoesNotExist) } returns Optional.empty()

        assertThrows<TemplateVersionDoesNotExistException> {
            templateVersionService.getTemplateVersion(
                templateVersionIdDoesNotExist
            )
        }
    }

    @Test
    fun itShouldReturnTemplateWhenCreatingTemplateVersion() {
        val templateIdExist = 1L

        val createTemplateVersionInput = CreateTemplateVersionInput(templateIdExist)

        val mockTemplateVersion = TemplateVersion().apply {
            templateId = templateIdExist
            status = DRAFT
        }

        every {
            templateVersionRepository.findByTemplateIdAndStatus(
                templateIdExist,
                DRAFT
            )
        } returns mockTemplateVersionTwoDraft()
        every { templateRepository.findById(templateIdExist) } returns Optional.of(mockTemplate())
        every { templateVersionRepository.save(mockTemplateVersion) } returns mockTemplateVersion
        every { objectMapper.writeValueAsString(any()) } returns ""

        val templateVersion = templateVersionService.createTemplateVersion(createTemplateVersionInput)

        assert(templateVersion.id == templateIdExist)
        assert(templateVersion.status == DRAFT)

    }

    @Test
    fun itShouldThrowAnExceptionWhenCreatingTemplateVersionWhereTemplateDoesNotExist() {
        val templateIdDoesNotExist = 0L

        val createTemplateVersionInputWithTemplateIdDoesNotExist = CreateTemplateVersionInput(templateIdDoesNotExist)

        every { templateVersionRepository.findByTemplateIdAndStatus(templateIdDoesNotExist, DRAFT) } returns null
        every { templateRepository.findById(templateIdDoesNotExist) } returns Optional.empty()

        assertThrows<TemplateVersionDoesNotExistException> {
            templateVersionService.createTemplateVersion(
                createTemplateVersionInputWithTemplateIdDoesNotExist
            )
        }
    }

    @Test
    fun itShouldReturnTemplateVersionWhenUpdatingTemplateVersion() {
        val templateVersionId = 1L
        val templateVersionName = "New Template Version Name"
        val templateVersionSettings = "{ \"to\": [\"receipient@company.com\"] }"
        val templateVersionBody = "Hi {{ username }}, this email is to inform you about your bill alert."

        val updatePublishTemplateVersionInput = UpdateTemplateVersionInput(
            templateVersionId,
            templateVersionName,
            templateVersionSettings,
            templateVersionBody,
            PUBLISHED,
            mockPluginsInput()
        )

        val mockPublishedTemplateVersion = TemplateVersion().apply {
            id = templateVersionId
            name = templateVersionName
            settings = templateVersionSettings
            body = templateVersionBody
            version = 3L
            status = PUBLISHED
        }

        every { templateVersionRepository.findById(templateVersionId) } returns Optional.of(mockTemplateVersionOneDraft())
        every { templateVersionRepository.save(any()) } returns mockPublishedTemplateVersion
        every { templateVersionRepository.findCurrentVersionNumberByTemplateId(1L) } returns 3L
        every {
            templatePluginService.createTemplatePlugins(
                templateVersionId,
                mockPluginsInput()
            )
        } returns mockTemplatePlugins()

        every {
            templateVersionRepository.existsByTemplateIdAndTemplateDigest(
                1L,
                mockPublishedTemplateVersion.templateDigest!!
            )
        } returns false

        val publishTemplateVersion = templateVersionService.updateTemplateVersion(updatePublishTemplateVersionInput)

        assert(publishTemplateVersion.id == templateVersionId)
        assert(publishTemplateVersion.status == PUBLISHED)
    }

    @Test
    fun itShouldThrowsExceptionWhenUpdatingTemplateVesionWhereTemplateVersionIdDoesNotExist() {
        val templateVersionIdDoesNotExist = 0L
        val templateVersionName = "New Template Version Name"
        val templateVersionSettings = "{ \"to\": [\"receipient@company.com\"] }"
        val templateVersionBody = "Hi {{ username }}, this email is to inform you about your bill alert."

        val missingTemplateVersionIdInput = UpdateTemplateVersionInput(
            templateVersionIdDoesNotExist,
            templateVersionName,
            templateVersionSettings,
            templateVersionBody,
            DRAFT,
            mockPluginsInput()
        )

        every { templateVersionRepository.findById(templateVersionIdDoesNotExist) } returns Optional.empty()

        assertThrows<TemplateVersionDoesNotExistException> {
            templateVersionService.updateTemplateVersion(
                missingTemplateVersionIdInput
            )
        }
    }

}