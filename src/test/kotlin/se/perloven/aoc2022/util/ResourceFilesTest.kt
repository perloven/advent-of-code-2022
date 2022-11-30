package se.perloven.aoc2022.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ResourceFilesTest {

    @Test
    fun readSampleFileLines() {
        val lines: List<String> = ResourceFiles.readLines("SampleFile.txt")

        assertEquals(5, lines.size)
    }

    @Test
    fun `Read non-existent file`() {
        assertThrows<IllegalArgumentException> {
            ResourceFiles.readLines("NonExistentFile.zip")
        }
    }
}