package se.perloven.aoc2022.day5

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SupplyStacksTest {

    @Test
    fun `Part one`() {
        assertEquals("TQRFCBSJJ", SupplyStacks.part1())
    }

    @Test
    fun `Part two`() {
        assertEquals("RMHFJNVFP", SupplyStacks.part2())
    }
}