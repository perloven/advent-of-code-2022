package se.perloven.aoc2022.day0

import se.perloven.aoc2022.util.ResourceFiles

fun main() {
    SampleSolution().solve()
}

class SampleSolution {

    fun solve() {
        val lines = ResourceFiles.readLines("day0/day0-sample.txt")
        val linesContainingLineNumber = lines.count { it.startsWith("line") }
        println("Day 0 solution: $linesContainingLineNumber lines start with \"line\"")
    }
}