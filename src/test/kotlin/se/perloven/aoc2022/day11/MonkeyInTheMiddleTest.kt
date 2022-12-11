package se.perloven.aoc2022.day11

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigInteger

class MonkeyInTheMiddleTest {

    @Test
    fun part1() {
        assertEquals(58794, MonkeyInTheMiddle.part1())
    }

    @Test
    fun part2() {
        assertEquals(BigInteger.valueOf(20151213744L), MonkeyInTheMiddle.part2())
    }
}