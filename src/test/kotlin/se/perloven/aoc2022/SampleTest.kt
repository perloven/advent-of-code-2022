package se.perloven.aoc2022

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class SampleTest {

    @Test
    fun workingTest() {
        assertTrue(true)
    }

    @Test
    @Disabled("Disable failing test, make build pass")
    fun failingTest() {
        assertFalse(true)
    }
}