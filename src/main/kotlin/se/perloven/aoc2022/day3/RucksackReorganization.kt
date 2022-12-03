package se.perloven.aoc2022.day3

import se.perloven.aoc2022.util.ResourceFiles

fun main() {
    println("Part 1: ${RucksackReorganization.part1()}")
    println("Part 2: ${RucksackReorganization.part2()}")
}

object RucksackReorganization {

    fun part1(): Int {
        val lines = ResourceFiles.readLines("day3/input-1.txt")
        return lines.sumOf { findPriorityOfMatchingItem(it) }
    }

    fun part2(): Int {
        val lines = ResourceFiles.readLines("day3/input-1.txt")
        return lines.chunked(3)
            .sumOf { findBadgePriority(it) }
    }

    private fun findBadgePriority(lines: List<String>): Int {
        return lines[0].find { it in lines[1] && it in lines[2] }?.let {
        println(it)
            getPriority(it)
        }!!
    }

    private fun findPriorityOfMatchingItem(line: String): Int {
        val (compartment1, compartment2) = divideRucksack(line)
        val matchingItem = findMatchingItem(compartment1, compartment2)
        return getPriority(matchingItem)
    }

    private fun divideRucksack(line: String): Pair<String, String> {
        val size = line.length
        val halfpoint = size / 2
        return Pair(line.substring(0, halfpoint), line.substring(halfpoint))
    }

    private fun findMatchingItem(compartment1: String, compartment2: String): Char {
        val items1 = compartment1.toList()
        val items2 = compartment2.toList()
        return items1.find { items2.contains(it) }!!
    }

    private fun getPriority(char: Char): Int {
        return if (char in CharRange(65.toChar(), 90.toChar())) {
            char.code - (65 - 27)
        } else if (char in CharRange(97.toChar(), 122.toChar())) {
            char.code - (97 - 1)
        } else {
            0
        }
    }
}