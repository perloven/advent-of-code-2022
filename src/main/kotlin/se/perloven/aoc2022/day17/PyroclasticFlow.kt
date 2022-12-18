package se.perloven.aoc2022.day17

import se.perloven.aoc2022.util.Position
import java.time.Duration
import java.time.Instant

fun main() {
    println("Part 1: ${PyroclasticFlow.part1()}")
    println("Part 2: ${PyroclasticFlow.part2()}")
}

object PyroclasticFlow {

    private class Chamber(
        private val jetPattern: JetPattern,
        private val height: Int = 10_000,
        private val rocksToDrop: Long = 2022
    ) {
        private companion object {
            private const val WIDTH = 9
        }
        private val rockChamber: Array<BooleanArray> = createRockChamber()
        private var highestRock: Long = 0
        private var droppedRocks: Int = 0
        private var prevRock: Rock = BoxRock(Position(0, -1))

        // walls at x=0, x=8
        // floor at y=0
        private fun createRockChamber(): Array<BooleanArray> {
            val rocks = Array(WIDTH) { BooleanArray(height) }

            // floor
            repeat(WIDTH - 1) { x ->
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
            if (droppedRocks >= rocksToDrop) {
                return false
            }

            val startPos = Position(3, highestRock.toInt() + 4)
            val rock = nextRock(startPos)
            //println("Rock starts at $startPos")

            var hasLanded = false
            while (!hasLanded) {
                val pushedRock = tryPushWithJet(rock, jetPattern.getNext())
                val didFall = fall(pushedRock)
                if (!didFall) {
                    hasLanded = true
                    placeRock(pushedRock)
                }
            }

            droppedRocks++
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

        private fun fall(rock: Rock): Boolean {
            val canFall = rock.moveDownReq.all { !isRock(rock.pos + it) }
            if (canFall) {
                rock.fall()
                return true
            }

            return false
        }

        private fun placeRock(rock: Rock) {
            //println("Placing rock ${rock::class.simpleName} at ${rock.pos}")
            val positionsToPlace = rock.covers.map { rock.pos + it }

            //println(toString())
            positionsToPlace.forEach {
                check(!isRock(it)) { "Trying to place ${rock::class.simpleName} at $it, but it's already rock!" }
                setRock(it)
                if (it.y > highestRock) {
                    highestRock = it.y.toLong()
                }
            }
        }

        private fun isRock(pos: Position): Boolean {
            return isRock(pos.x, pos.y)
        }

        private fun isRock(x: Int, y: Int): Boolean {
            return rockChamber[x][y]
        }

        private fun setRock(pos: Position) {
            rockChamber[pos.x][pos.y] = true
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

        fun getDroppedRocks(): Int {
            return droppedRocks
        }

        fun getRockHeight(): Long {
            return highestRock
        }

        override fun toString(): String {
            var string = "Highest: $highestRock\n"
            for (y in (highestRock + 5) downTo 0) {
                var row = "$y\t\t"
                repeat(rockChamber.size) { x ->
                    val char = if (rockChamber[x][y.toInt()]) '#' else '.'
                    row += char
                }
                string += row + "\n"
            }
            return string.take(300)
        }

        fun findFilledRows(): List<Int> {
            return (1 until height).filter { y -> isRowFilled(y) }
        }

        private fun isRowFilled(y: Int): Boolean {
            return (0 until WIDTH).all { x -> isRock(x, y) }
        }

        fun lastRock(): Rock {
            return prevRock
        }
    }

    // 4448 is too high
    fun part1(): Long {
        val jetPattern = parseJetPattern()
        //println("Jets: ${jetPattern.size()}")
        //println(jetPattern)

        val rocks = 2022L
        val chamber = Chamber(jetPattern = jetPattern, height = 2_000_000, rocksToDrop = rocks)

        val before = Instant.now()
        while (chamber.dropNextRock()) {
            // Do nothing, chamber handles logic
        }
        val duration = Duration.between(before, Instant.now())
        //println("Filled rows: ${chamber.findFilledRows()}")
        //println("$rocks rocks dropped in ${duration.toMillis()} ms")
        //println(chamber.toString())

        return chamber.getRockHeight()
    }

    // correct answer: 1553982300884
    // TODO: code a way to end up with this answer. I did it with math by hand.
    fun part2(): Long {
        val jetPattern = parseJetPattern()
        //println(jetPattern)

        val chamber = Chamber(jetPattern = jetPattern, rocksToDrop = 50455, height = 1_000_000)

        var prevJetIndex = jetPattern.index()
        while (chamber.dropNextRock()) {
            val droppedRocks = chamber.getDroppedRocks()
            if (droppedRocks == 1728 || droppedRocks == 2500) {
                println("Dropped rocks: $droppedRocks, height: ${chamber.getRockHeight()}")
            }
            val curJetIndex = jetPattern.index()
            if (curJetIndex < prevJetIndex) {
                //println("Jet pattern repeats! Last rock: ${chamber.lastRock()::class.simpleName}, dropped rocks: ${chamber.getDroppedRocks()}")
                //println(chamber)
            }
            prevJetIndex = curJetIndex
        }
        println(chamber.toString())
        println("Jet index: ${jetPattern.index()}")
        println("Last rock: ${chamber.lastRock()::class.simpleName}")

        return chamber.getRockHeight()
    }
}