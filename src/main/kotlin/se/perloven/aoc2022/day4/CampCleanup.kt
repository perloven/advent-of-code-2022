package se.perloven.aoc2022.day4

import se.perloven.aoc2022.util.ResourceFiles

fun main() {
    println("Part 1: ${CampCleanup.part1()}")
    println("Part 2: ${CampCleanup.part2()}")
}

object CampCleanup {

    fun part1(): Int {
        val lines = ResourceFiles.readLinesSplit(4, delimiter = ",")
        return lines.sumOf { getLineValueContains(it) }
    }

    fun part2(): Int {
        val lines = ResourceFiles.readLinesSplit(4, delimiter = ",")
        return lines.sumOf { getLineValueOverlaps(it) }
    }

    private fun getLineValueContains(line: List<String>): Int {
        val ranges = line.map { toRange(it) }
        val range1 = ranges[0]
        val range2 = ranges[1]
        return if (ranges.any { oneContainsTheOther(range1, range2) }) 1 else 0
    }

    private fun oneContainsTheOther(first: IntRange, second: IntRange): Boolean {
        return isFullyContainedBy(first, second) || isFullyContainedBy(second, first)
    }

    private fun isFullyContainedBy(first: IntRange, second: IntRange): Boolean {
        return first.first >= second.first && first.last <= second.last
    }

    private fun getLineValueOverlaps(line: List<String>): Int {
        val ranges = line.map { toRange(it) }
        val range1 = ranges[0]
        val range2 = ranges[1]
        return if (ranges.any { overlaps(range1, range2) }) 1 else 0
    }

    private fun overlaps(first: IntRange, second: IntRange): Boolean {
        return first.first in second || first.last in second
                || second.first in first || second.last in first
    }

    private fun toRange(lineSegment: String): IntRange {
        val parts = lineSegment.split("-")
        return IntRange(parts[0].toInt(), parts[1].toInt())
    }
}