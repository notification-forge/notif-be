package com.forge.messageservice.common.files

import com.forge.messageservice.entities.Image

object SimilarFilenameGenerator {

    private val fileNamePattern =
        "^(?<filename>[a-zA-Z0-9]+)(\\((?<fileIndex>\\d+)\\))?\\.(?<ext>[a-zA-Z0-9]+)$".toRegex()

    /**
     * Generates a new filename similar to the `filename` parameter but with index on it.
     *
     * Example:
     * image.png -> image (1).png
     *
     * If there are images with similar (already indexed) names, generate a filename that uses the
     * highest index in the group + 1
     *
     * Example
     * image(1).png -> image(2).png
     */
    fun generateFilename(filename: String, similarImages: List<Image>): String {
        val index = highestIndexInGroupOf(similarImages)

        val matchResult = fileNamePattern.matchEntire(filename)
        if (matchResult != null) {
            return "${matchResult.groups["filename"]?.value}(${index + 1}).${matchResult.groups["ext"]?.value}"
        }

        return filename
    }

    private fun highestIndexInGroupOf(similarImages: List<Image>): Int {
        return similarImages
            // check if filename matches our file pattern
            .mapNotNull { m -> fileNamePattern.matchEntire(m.fileName) }
            // check if file pattern contains file index
            .mapNotNull { m -> m.groups["fileIndex"] }
            // find highest index
            .maxByOrNull { z -> z.value.toInt() }?.value?.toInt() ?: 0
    }
}