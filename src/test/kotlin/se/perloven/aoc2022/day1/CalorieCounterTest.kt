package se.perloven.aoc2022.day1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CalorieCounterTest {
    private val calorieCounter = CalorieCounter()

    @Test
    fun countMax() {
        assertEquals(69_883, calorieCounter.countMax())
    }

    @Test
    fun countTopThree() {
        assertEquals(207_576, calorieCounter.countTopThree())
    }
}