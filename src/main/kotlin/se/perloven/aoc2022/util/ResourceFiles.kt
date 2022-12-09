package se.perloven.aoc2022.util

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists

object ResourceFiles {
    private val inputBasePath = Path.of("src", "main", "resources", "input")

    /**
     * Read all lines from a file located in "resources".
     * Each line corresponds to one element in the resulting list.
     */
    fun readLines(day: Int): List<String> {
        val fileName = "day$day.txt"
        val path = inputBasePath.resolve(fileName)
        require(path.exists()) { "Resource file $fileName does not exist"}
        return Files.lines(path).toList()
    }

    /**
     * Read all lines from a file located in "resources",
     * and split them into multiple smaller lists according to some rule (provided by the caller).
     */
    fun readLinesDivided(day: Int, splitRule: (String) -> Boolean): List<List<String>> {
        val splitLists = mutableListOf<List<String>>()

        val lines = readLines(day)
        var currentList = mutableListOf<String>()
        lines.forEach {
            if (splitRule(it)) {
                splitLists.add(currentList)
                currentList = mutableListOf()
            } else {
                currentList.add(it)
            }
        }

        return splitLists
    }

    fun readLinesSplit(day: Int, delimiter: String = " "): List<List<String>> {
        return readLines(day).map { it.split(delimiter) }
    }
}