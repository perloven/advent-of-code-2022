package se.perloven.aoc2022.day10

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class CathodeRayTubeTest {

    @Test
    fun part1() {
        assertEquals(13720, CathodeRayTube.part1())
    }

    @Test
    fun part2() {
        assertEquals("""
            
            ####.###..#..#.###..#..#.####..##..#..#.
            #....#..#.#..#.#..#.#..#....#.#..#.#..#.
            ###..###..#..#.#..#.####...#..#....####.
            #....#..#.#..#.###..#..#..#...#....#..#.
            #....#..#.#..#.#.#..#..#.#....#..#.#..#.
            #....###...##..#..#.#..#.####..##..#..#.
        """.trimIndent(), CathodeRayTube.part2())
    }
}