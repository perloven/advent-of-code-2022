package se.perloven.aoc2022.day18

import se.perloven.aoc2022.util.ResourceFiles

fun main() {
    println("Part 1: ${BoilingBoulders.part1()}")
    println("Part 2: ${BoilingBoulders.part2()}")
}

object BoilingBoulders {

    private data class Position3D(val x: Int, val y: Int, val z: Int) {
        operator fun plus(other: Position3D): Position3D {
            return Position3D(x + other.x, y + other.y, z + other.z)
        }
    }

    private class Grid3D(private val pos: List<Position3D>) {
        private val grid: Array<Array<BooleanArray>>
        private val checks = setOf(
            Position3D(1, 0, 0), Position3D(-1, 0, 0), // x+, x- (right, left)
            Position3D(0, 1, 0), Position3D(0, -1, 0), // y+, y- (above, below)
            Position3D(0, 0, 1), Position3D(0, 0, -1)  // z+, z- (in front, behind)
        )

        init {
            val maxX = pos.maxOf { it.x }
            val maxY = pos.maxOf { it.y }
            val maxZ = pos.maxOf { it.z }
            grid = Array(maxX + 5) { Array(maxY + 5) { BooleanArray(maxZ + 5) } }

            pos.forEach {
                val adjustedPos = adjustedPos(it)
                grid[adjustedPos.x][adjustedPos.y][adjustedPos.z] = true
            }
        }

        fun totalExposedSides(): Int {
            return pos.sumOf { exposedSides(it) }
        }

        private fun exposedSides(pos: Position3D): Int {
            val adjustedPos = adjustedPos(pos)
            return checks.count {
                val testPos = adjustedPos + it
                !grid[testPos.x][testPos.y][testPos.z]
            }
        }

        private fun adjustedPos(pos: Position3D): Position3D {
            return Position3D(pos.x + 1, pos.y + 1, pos.z + 1)
        }
    }

    fun part1(): Int {
        val pos3ds = parsePosition3ds()

        val grid = Grid3D(pos3ds)

        return grid.totalExposedSides()
    }

    private fun parsePosition3ds(): List<Position3D> {
        return ResourceFiles.readLinesSplit(18, ",")
            .map {
                Position3D(
                    x = it[0].toInt(),
                    y = it[1].toInt(),
                    z = it[2].toInt()
                )
            }
    }

    fun part2(): Int {
        return -2
    }
}