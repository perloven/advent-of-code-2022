package se.perloven.aoc2022.day7

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class NoSpaceLeftOnDeviceTest {

    @Test
    fun part1() {
        assertEquals(1453349, NoSpaceLeftOnDevice.part1())
    }

    @Test
    fun part2() {
        assertEquals(2948823, NoSpaceLeftOnDevice.part2())
    }
}