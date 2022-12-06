package se.perloven.aoc2022.day6

import se.perloven.aoc2022.util.ResourceFiles

fun main() {
    println("Part 1: ${Day6.part1()}")
    println("Part 2: ${Day6.part2()}")
}

object Day6 {


    fun part1(): Int {
        val input = ResourceFiles.readLines("day6/input-1.txt")[0].toList()
        for (i in 3..input.size) {
            val set = setOf(input[i - 3], input[i - 2], input[i - 1], input[i])
            if (set.size == 4) {
                return i + 1
            }
        }

        return -1
    }

    fun part2(): Int {
        val input = ResourceFiles.readLines("day6/input-1.txt")[0].toList()
        for (i in 13..input.size) {
            val set = mutableSetOf<Char>()
            repeat(14) {
                set.add(input[i - it])
            }
            if (set.size == 14) {
                return i + 1
            }
        }

        return -1
    }
}