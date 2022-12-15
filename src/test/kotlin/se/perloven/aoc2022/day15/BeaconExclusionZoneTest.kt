package se.perloven.aoc2022.day15

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class BeaconExclusionZoneTest {

    @Test
    fun part1() {
        assertEquals(5147333, BeaconExclusionZone.part1())
    }

    @Test
    @Disabled("GHA runs out of memory")
    fun part2() {
        assertEquals(13734006908372L, BeaconExclusionZone.part2())
    }
}