package com.forge.messageservice.common.extensions

import com.forge.messageservice.common.files.SimilarFilenameGenerator
import com.forge.messageservice.common.files.SimilarFilenameGenerator.generateFilename
import com.forge.messageservice.entity.Image
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SimilarFilenameGeneratorTest {

    @Test
    fun itShouldReturnAnIndexedFilename() {
        assertEquals(
            "hello(1).png",
            generateFilename("hello.png", emptyList())
        )

        assertEquals(
            "hello(2).png",
            generateFilename("hello.png", listOf(Image().apply { fileName = "hello(1).png" }))
        )
    }

}