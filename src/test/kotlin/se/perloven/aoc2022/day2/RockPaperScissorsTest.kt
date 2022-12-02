package se.perloven.aoc2022.day2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RockPaperScissorsTest {
    private val rockPaperScissors = RockPaperScissors()

    @Test
    fun `Part one`() {
        assertEquals(13052, rockPaperScissors.countRPSScore())
    }

    @Test
    fun `Part two`() {
        assertEquals(13693, rockPaperScissors.countRPSScore2())
    }
}