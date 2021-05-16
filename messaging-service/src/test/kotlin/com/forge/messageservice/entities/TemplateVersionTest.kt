package com.forge.messageservice.entities

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class TemplateVersionTest {

    @Test
    fun itGeneratesSha256HashOfTheSettingsAndMessageBody() {
        val templateVersion = TemplateVersion().apply {
            settings = "{}"
            body = "{}"
        }

        Assertions.assertNotNull(templateVersion.templateDigest)
    }

}