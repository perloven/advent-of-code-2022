package se.perloven.aoc2022.util

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists

object ResourceFiles {
    private val basePath = Path.of("src", "main", "resources")

    fun readLines(fileName: String): List<String> {
        val path = basePath.resolve(fileName)
        require(path.exists()) { "Resource file $fileName does not exist"}
        return Files.lines(path).toList()
    }
}