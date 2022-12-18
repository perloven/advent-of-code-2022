package se.perloven.aoc2022.util

data class Position(val x: Int, val y: Int) {
    fun with(newX: Int = x, newY: Int = y): Position {
        return Position(newX, newY)
    }

    override fun toString(): String {
        return "($x,$y)"
    }

    operator fun plus(other: Position): Position {
        require((x.toLong() + other.x.toLong()) < Int.MAX_VALUE) { "x is overflowing" }
        require((y.toLong() + other.y.toLong()) < Int.MAX_VALUE) { "y is overflowing" }
        return Position(x + other.x, y + other.y)
    }
}