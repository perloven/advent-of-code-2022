package se.perloven.aoc2022.day17

import se.perloven.aoc2022.util.Position

sealed class Rock(var pos: Position) {
    abstract val covers: Set<Position>
    abstract val moveLeftReq: Set<Position>
    abstract val moveRightReq: Set<Position>
    abstract val moveDownReq: Set<Position>

    fun moveLeft(): Position {
        val newPos = Position(pos.x - 1, pos.y)
        this.pos = newPos
        return newPos
    }

    fun moveRight(): Position {
        val newPos = Position(pos.x + 1, pos.y)
        this.pos = newPos
        return newPos
    }

    fun fall(): Position {
        val newPos = Position(pos.x, pos.y - 1)
        this.pos = newPos
        return newPos
    }
}

class FlatRock(pos: Position) : Rock(pos) {
    override val covers = setOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(3, 0))
    override val moveLeftReq = setOf(Position(-1, 0))
    override val moveRightReq = setOf(Position(4, 0))
    override val moveDownReq = setOf(Position(0, -1), Position(1, -1), Position(2, -1), Position(3, -1))
}

class PlusRock(pos: Position) : Rock(pos) {
    override val covers = setOf(Position(1, 0), Position(0, 1), Position(1, 1), Position(2, 1), Position(1, 2))
    override val moveLeftReq = setOf(Position(0, 0), Position(-1, 1), Position(0, 2))
    override val moveRightReq = setOf(Position(2, 0), Position(3, 1), Position(2, 2))
    override val moveDownReq = setOf(Position(0, 0), Position(1, -1), Position(2, 0))
}

class StepRock(pos: Position) : Rock(pos) {
    override val covers = setOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(2, 1), Position(2, 2))
    override val moveLeftReq = setOf(Position(-1, 0), Position(1, 1), Position(1, 2))
    override val moveRightReq = setOf(Position(3, 0), Position(3, 1), Position(3, 2))
    override val moveDownReq = setOf(Position(0, -1), Position(1, -1), Position(2, -1))
}

class StandingRock(pos: Position) : Rock(pos) {
    override val covers = setOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(0, 3))
    override val moveLeftReq = setOf(Position(-1, 0), Position(-1, 1), Position(-1, 2), Position(-1, 3))
    override val moveRightReq = setOf(Position(1, 0), Position(1, 1), Position(1, 2), Position(1, 3))
    override val moveDownReq = setOf(Position(0, -1))
}

class BoxRock(pos: Position) : Rock(pos) {
    override val covers = setOf(Position(0, 0), Position(1, 0), Position(0, 1), Position(1, 1))
    override val moveLeftReq = setOf(Position(-1, 0), Position(-1, 1))
    override val moveRightReq = setOf(Position(2, 0), Position(2, 1))
    override val moveDownReq = setOf(Position(0, -1), Position(1, -1))
}