package se.perloven.aoc2022.day6

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TuningTroubleTest {

    @Test
    fun `Part one`() {
        assertEquals(1538, TuningTrouble.part1())
    }

    @Test
    fun `Part two`() {
        assertEquals(2315, TuningTrouble.part2())
    }
}