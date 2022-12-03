package se.perloven.aoc2022.day3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RucksackReorganizationTest {

    @Test
    fun `Part one`() {
        assertEquals(7763, RucksackReorganization.part1())
    }

    @Test
    fun `Part two`() {
        assertEquals(2569, RucksackReorganization.part2())
    }
}