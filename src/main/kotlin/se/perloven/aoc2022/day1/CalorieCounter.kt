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
        val input = ResourceFiles.readLines("day1/input-1.txt")
        return splitListOnBlanks(input)
            .maxOf { sublist -> sublist.sumOf { it.toInt() } }
    }

    fun countTopThree(): Int {
        val input = ResourceFiles.readLines("day1/input-1.txt")
        return splitListOnBlanks(input)
            .map { sublist -> sublist.sumOf { it.toInt() } }
            .sortedDescending()
            .slice(0..2)
            .sum()
    }

    private fun splitListOnBlanks(input: List<String>): List<List<String>> {
        val listOfLists = mutableListOf<List<String>>()

        var currentList = mutableListOf<String>()
        input.forEach {
            if (it.isBlank()) {
                listOfLists.add(currentList)
                currentList = mutableListOf()
            } else {
                currentList.add(it)
            }
        }

        return listOfLists
    }
}