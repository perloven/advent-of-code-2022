package se.perloven.aoc2022.day1

import se.perloven.aoc2022.util.ResourceFiles

fun main() {
    val calorieCounter = CalorieCounter()

    val maxCalories: Int = calorieCounter.countMax()
    println("Most amount of calories: $maxCalories")

    val topThreeMaxCalories: Int = calorieCounter.countTopThree()
    println("Top three amount of calories summed: $topThreeMaxCalories")
}

class CalorieCounter {

    fun countMax(): Int {
        return splitListOnBlanks()
            .maxOf { sublist -> sublist.sumOf { it.toInt() } }
    }

    fun countTopThree(): Int {
        return splitListOnBlanks()
            .map { sublist -> sublist.sumOf { it.toInt() } }
            .sortedDescending()
            .slice(0..2)
            .sum()
    }

    private fun splitListOnBlanks(): List<List<String>> {
        return ResourceFiles.readLinesDivided("day1/input-1.txt") { it.isEmpty() }
    }
}