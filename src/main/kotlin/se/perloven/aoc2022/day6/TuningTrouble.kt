package se.perloven.aoc2022.day6

import se.perloven.aoc2022.util.ResourceFiles

fun main() {
    println("Part 1: ${TuningTrouble.part1()}")
    println("Part 2: ${TuningTrouble.part2()}")
}

object TuningTrouble {


    fun part1(): Int {
        return findUniqueSequence(4)
    }

    fun part2(): Int {
        return findUniqueSequence(14)
    }

    private fun findUniqueSequence(seqLength: Int): Int {
        val input = ResourceFiles.readLines("day6/input-1.txt")[0].toList()
        for (i in (seqLength - 1)..input.size) {
            val set = mutableSetOf<Char>()
            repeat(seqLength) {
                set.add(input[i - it])
            }
            if (set.size == seqLength) {
                return i + 1
            }
        }

        return -1
    }
}