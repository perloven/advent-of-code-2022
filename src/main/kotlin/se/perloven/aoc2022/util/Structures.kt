package se.perloven.aoc2022.util

data class Position(val x: Int, val y: Int) {
    fun with(newX: Int = x, newY: Int = y): Position {
        return Position(newX, newY)
    }
}