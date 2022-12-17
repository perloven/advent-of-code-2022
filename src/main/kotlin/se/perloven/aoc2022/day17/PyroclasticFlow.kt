package se.perloven.aoc2022.day17

import se.perloven.aoc2022.util.Position
import se.perloven.aoc2022.util.ResourceFiles

fun main() {
    println("Part 1: ${PyroclasticFlow.part1()}")
    println("Part 2: ${PyroclasticFlow.part2()}")
}

object PyroclasticFlow {

    private sealed class Rock(var pos: Position) {
        abstract val covers: Set<Position>
        abstract val moveLeftReq: Set<Position>
        abstract val moveRightReq: Set<Position>
        abstract val moveDownReq: Set<Position>

        fun moveLeft(): Position {
            return Position(pos.x - 1, pos.y).also {
                this.pos = it
            }
        }

        fun moveRight(): Position {
            return Position(pos.x + 1, pos.y).also {
                this.pos = it
            }
        }

        fun moveDown(): Position {
            return Position(pos.x, pos.y - 1).also {
                this.pos = it
            }
        }
    }
    private class FlatRock(pos: Position): Rock(pos) {
        override val covers = setOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(3, 0))
        override val moveLeftReq = setOf(Position(-1, 0))
        override val moveRightReq = setOf(Position(4, 0))
        override val moveDownReq = setOf(Position(0, -1), Position(1, -1), Position(2, -1), Position(3, -1))
    }
    private class PlusRock(pos: Position): Rock(pos) {
        override val covers = setOf(Position(1, 0), Position(0, 1), Position(1, 1), Position(2, 1), Position(1, 2))
        override val moveLeftReq = setOf(Position(0, 0), Position(-1, 1), Position(0, 2))
        override val moveRightReq = setOf(Position(2, 0), Position(3, 1), Position(2, 2))
        override val moveDownReq = setOf(Position(0, 0), Position(1, -1), Position(2, 0))
    }
    private class StepRock(pos: Position): Rock(pos) {
        override val covers = setOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(2, 1), Position(2, 2))
        override val moveLeftReq = setOf(Position(-1, 0), Position(1, 1), Position(1, 2))
        override val moveRightReq = setOf(Position(3, 0), Position(3, 1), Position(3, 2))
        override val moveDownReq = setOf(Position(0, -1), Position(1, -1), Position(2, -1))
    }
    private class StandingRock(pos: Position): Rock(pos) {
        override val covers = setOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(0, 3))
        override val moveLeftReq = setOf(Position(-1, 0), Position(-1, 1), Position(-1, 2), Position(-1, 3))
        override val moveRightReq = setOf(Position(1, 0), Position(1, 1), Position(1, 2), Position(1, 3))
        override val moveDownReq = setOf(Position(0, -1))
    }
    private class BoxRock(pos: Position): Rock(pos) {
        override val covers = setOf(Position(0, 0), Position(1, 0), Position(0, 1), Position(1, 1))
        override val moveLeftReq = setOf(Position(-1, 0), Position(-1, 1))
        override val moveRightReq = setOf(Position(2, 0), Position(2, 1))
        override val moveDownReq = setOf(Position(0, -1), Position(1, 1))
    }

    private enum class Jet {
        PUSH_LEFT,
        PUSH_RIGHT;

        override fun toString(): String {
            return when (this) {
                PUSH_LEFT -> "<"
                PUSH_RIGHT -> ">"
            }
        }
    }

    private class JetPattern(private val jets: List<Jet>) {
        private var index: Int = 0

        fun getNext(): Jet {
            if (index >= jets.size) {
                index = 0
            }
            return jets[index++]
        }

        override fun toString(): String {
            return jets.joinToString(separator = " ")
        }
    }

    private class Chamber(private val jets: List<Jet>) {
        private val rockChamber: Array<BooleanArray> = createRockChamber()
        private var highestRock: Int = 0
        private var droppedRocks: Int = 0
        private var prevRock: Rock = BoxRock(Position(0, -1))

        // walls at x=0, x=8
        // floor at y=0
        private fun createRockChamber() : Array<BooleanArray> {
            val width = 9
            val height = 10_000
            val rocks = Array(width) { BooleanArray(height) }

            // floor
            repeat(width - 1) { x ->
                rocks[x][0] = true
            }

            // walls
            repeat(height - 1) { y ->
                rocks[0][y] = true
                rocks[8][y] = true
            }

            return rocks
        }

        fun dropNextRock(): Boolean {
            if (droppedRocks++ >= 2022) {
                return false
            }

            val startPos = Position(3, highestRock + 4)
            val rock = nextRock(startPos)

            val jetPatterns = JetPattern(jets)
            var rockHasLanded = false
            while(!rockHasLanded) {
                tryPushWithJet(rock, jetPatterns.getNext())
                val canMoveDown = tryMoveDown(rock)
                if (!canMoveDown) {
                    placeRock(rock)
                    rockHasLanded = true
                }
            }

            return true
        }

        private fun tryPushWithJet(rock: Rock, jet: Jet): Rock {
            return when (jet) {
                Jet.PUSH_LEFT -> tryPushLeft(rock)
                Jet.PUSH_RIGHT -> tryPushRight(rock)
            }
        }

        private fun tryPushLeft(rock: Rock): Rock {
            val canMove = rock.moveLeftReq.all { !isRock(rock.pos + it) }
            if (canMove) {
                rock.moveLeft()
            }

            return rock
        }

        private fun tryPushRight(rock: Rock): Rock {
            val canMove = rock.moveRightReq.all { !isRock(rock.pos + it) }
            if (canMove) {
                rock.moveRight()
            }

            return rock
        }

        private fun tryMoveDown(rock: Rock): Boolean {
            val cannotMoveDown = rock.moveDownReq.any { isRock(rock.pos + it) }
            if (cannotMoveDown) {
                return false
            }

            rock.moveDown()
            return true
        }

        private fun placeRock(rock: Rock) {
            println("Placing rock ${rock::class.simpleName} at ${rock.pos}")
            val positions = rock.covers.map { rock.pos + it }

            positions.forEach {
                check(!rockChamber[it.x][it.y]) { "Trying to place rock at $it, it's already rock!" }
                rockChamber[it.x][it.y] = true
                if (it.y > highestRock) {
                    highestRock = it.y
                }
            }
        }

        private fun isRock(pos: Position): Boolean {
            return rockChamber[pos.x][pos.y]
        }

        private fun nextRock(pos: Position): Rock {
            val nextRock = when (prevRock) {
                is FlatRock -> PlusRock(pos)
                is PlusRock -> StepRock(pos)
                is StepRock -> StandingRock(pos)
                is StandingRock -> BoxRock(pos)
                is BoxRock -> FlatRock(pos)
            }
            prevRock = nextRock
            return nextRock
        }

        fun getHeight(): Int {
            return highestRock
        }
    }

    // 4448 is too high
    fun part1(): Int {
        val jetPattern = parseJetPattern()
        println(jetPattern)

        val chamber = Chamber(jetPattern)

        while (chamber.dropNextRock()) {
            // Do nothing, chamber handles logic
        }

        return chamber.getHeight()
    }

    private fun parseJetPattern(): List<Jet> {
        return ResourceFiles.readLines(17)[0].map { parseJet(it) }
    }

    private fun parseJet(char: Char): Jet {
        return when (char) {
            '<' -> Jet.PUSH_LEFT
            '>' -> Jet.PUSH_RIGHT
            else -> throw IllegalArgumentException("Invalid jet $char")
        }
    }

    fun part2(): Int {
        return -2
    }
}