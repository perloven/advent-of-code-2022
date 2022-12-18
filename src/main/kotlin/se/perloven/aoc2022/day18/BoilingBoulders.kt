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

    private enum class Material {
        WATER,
        LAVA,
        AIR
    }

    private class Grid3D(private val pos: List<Position3D>) {
        private val grid: Array<Array<Array<Material>>>
        private val checks = setOf(
            Position3D(1, 0, 0), Position3D(-1, 0, 0), // x+, x- (right, left)
            Position3D(0, 1, 0), Position3D(0, -1, 0), // y+, y- (above, below)
            Position3D(0, 0, 1), Position3D(0, 0, -1)  // z+, z- (in front, behind)
        )
        private val width: Int
        private val height: Int
        private val depth: Int

        init {
            val maxX = pos.maxOf { it.x }
            width = maxX + 5
            val maxY = pos.maxOf { it.y }
            height = maxY + 5
            val maxZ = pos.maxOf { it.z }
            depth = maxZ + 5
            grid = Array(width) { Array(height) { Array(depth) { Material.AIR } } }

            pos.forEach {
                val adjustedPos = adjustedPos(it)
                grid[adjustedPos.x][adjustedPos.y][adjustedPos.z] = Material.LAVA
            }

            floodWater()
        }

        fun totalExposedSides(): Int {
            return pos.sumOf { exposedSides(it) }
        }

        private fun exposedSides(pos: Position3D): Int {
            val adjustedPos = adjustedPos(pos)
            return checks.count {
                isExposed(adjustedPos + it)
            }
        }

        private fun isExposed(pos: Position3D): Boolean {
            val material = grid[pos.x][pos.y][pos.z]
            return material == Material.WATER || material == Material.AIR
        }

        private fun adjustedPos(pos: Position3D): Position3D {
            return Position3D(pos.x + 1, pos.y + 1, pos.z + 1)
        }

        fun totalExposedSidesFromOutside(): Int {
            // 1. fill pockets
            // 2. return totalExposedSides()
            return pos.sumOf { exposedSidesFromOutside(it) }
        }

        private fun exposedSidesFromOutside(pos: Position3D): Int {
            val adjustedPos = adjustedPos(pos)
            return checks.count {
                isExposedFromOutside(adjustedPos + it)
            }
        }

        private fun isExposedFromOutside(pos: Position3D): Boolean {
            val material = grid[pos.x][pos.y][pos.z]
            return material == Material.WATER
        }

        private fun floodWater() {
            grid[width-1][height-1][depth-1] = Material.WATER
            while (floodIteration()) {
                // Do nothing, floodIteration handles conversion of air to water
            }
        }

        // iterate all positions while changes have been made
        // if the position is water and has adjacent air
        //      -> adjacent is now water
        private fun floodIteration(): Boolean {
            var airWasConverted = false
            repeat(width) { x ->
                repeat(height) { y ->
                    repeat(depth) { z ->
                        val curPos = Position3D(x, y, z)
                        if (getMaterial(curPos) == Material.WATER) {
                            val toConvert = checks
                                .map { curPos + it }
                                .filter { isInGrid(it) }
                                .filter { getMaterial(it) == Material.AIR }
                            if (toConvert.isNotEmpty()) {
                                airWasConverted = true
                            }
                            toConvert.forEach {
                                grid[it.x][it.y][it.z] = Material.WATER
                            }
                        }
                    }
                }
            }

            return airWasConverted
        }

        private fun isInGrid(pos: Position3D): Boolean {
            return pos.x in (0 until width)
                    && pos.y in (0 until height)
                    && pos.z in (0 until depth)
        }

        private fun getMaterial(pos: Position3D): Material {
            return grid[pos.x][pos.y][pos.z]
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
        val pos3d = parsePosition3ds()

        val grid = Grid3D(pos3d)

        return grid.totalExposedSidesFromOutside()
    }
}