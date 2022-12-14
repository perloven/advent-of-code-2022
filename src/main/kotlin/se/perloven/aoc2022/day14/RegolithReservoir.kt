package se.perloven.aoc2022.day14

import se.perloven.aoc2022.util.Position
import se.perloven.aoc2022.util.ResourceFiles
import kotlin.math.max
import kotlin.math.min

fun main() {
    println("Part 1: ${RegolithReservoir.part1()}")
    println("Part 2: ${RegolithReservoir.part2()}")
}

object RegolithReservoir {

    private enum class Material {
        AIR,
        WALL,
        SAND
    }

    private class World (wallPaths: List<List<Position>>){
        private val grid: Array<Array<Material>>
        private val xBound: IntRange
        private val yBound: IntRange

        init {
            val (min, max) = findWorldBounds(wallPaths)
            this.xBound = 0..max.x + 1
            this.yBound = 0..max.y + 1
            //println("World bounds - min: $min, max: $max")
            this.grid = Array(max.x + 1) { Array(max.y + 10) { Material.AIR } }
            wallPaths.forEach { setWalls(it) }
        }

        private fun setWalls(wallPath: List<Position>) {
            var prevPosition = wallPath.first()
            for (pos in wallPath) {
                verifyStraightLine(prevPosition, pos)
                if (xDiffers(prevPosition, pos)) {
                    setHorizontalWall(pos.y, prevPosition.x, pos.x)
                } else if (yDiffers(prevPosition, pos)) {
                    setVerticalWall(pos.x, prevPosition.y, pos.y)
                }

                prevPosition = pos
            }
        }

        private fun setHorizontalWall(y: Int, x1: Int, x2: Int) {
            val start = min(x1, x2)
            val end = max(x1, x2)
            for (x in start..end) {
                grid[x][y] = Material.WALL
            }
        }

        private fun setVerticalWall(x: Int, y1: Int, y2: Int) {
            val start = min(y1, y2)
            val end = max(y1, y2)
            for (y in start..end) {
                grid[x][y] = Material.WALL
            }
        }

        private fun verifyStraightLine(pos1: Position, pos2: Position) {
            require(pos1.x == pos2.x || pos1.y == pos2.y) {
                "Non-straight wall between $pos1 and $pos2"
            }
        }

        private fun xDiffers(pos1: Position, pos2: Position): Boolean {
            return pos1.y == pos2.y && pos1.x != pos2.x
        }

        private fun yDiffers(pos1: Position, pos2: Position): Boolean {
            return pos1.x == pos2.x && pos1.y != pos2.y
        }

        fun dropSand(): Position? {
            var curPos: Position? = Position(500, 0)
            while(curPos != null) {
                val newPos = tryMoveSand(curPos)
                if (newPos == curPos) {
                    return curPos
                }
                curPos = newPos
            }

            return curPos
        }

        // Falling:
        // 1. Down
        // 2. Diagonally down-left
        // 3. Diagonally down-right
        private fun tryMoveSand(pos: Position): Position? {
            if (outOfBounds(pos)) {
                return null
            }

            if (downPossible(pos)) {
                return tryMoveSand(Position(pos.x, pos.y + 1))
            } else if (downLeftPossible(pos)) {
                return tryMoveSand(Position(pos.x - 1, pos.y + 1))
            } else if (downRightPossible(pos)) {
                return tryMoveSand(Position(pos.x + 1, pos.y + 1))
            } else {
                // come to rest
                grid[pos.x][pos.y] = Material.SAND
                return pos
            }
        }

        private fun downPossible(pos: Position): Boolean {
            val newY = pos.y + 1
            if (newY !in yBound) {
                return true
            }
            val materialDown = grid[pos.x][newY]
            return materialDown == Material.AIR
        }

        private fun downLeftPossible(pos: Position): Boolean {
            val newY = pos.y + 1
            if (newY !in yBound) {
                return true
            }
            val materialDownLeft = grid[pos.x - 1][newY]
            return materialDownLeft == Material.AIR
        }

        private fun downRightPossible(pos: Position): Boolean {
            val newX = pos.x + 1
            val newY = pos.y + 1
            if (newX !in xBound || newY !in yBound) {
                return true
            }
            val materialDownRight = grid[newX][newY]
            return materialDownRight == Material.AIR
        }

        private fun outOfBounds(pos: Position): Boolean {
            return pos.x !in xBound || pos.y !in yBound
        }

        fun countTotalMaterial(searched: Material = Material.SAND): Int {
            var count = 0
            for (col in grid) {
                for (material in col) {
                    if (material == searched) {
                        count++
                    }
                }
            }

            return count
        }
    }

    fun part1(): Int {
        // 1. Parse paths
        val paths = parsePaths()
        //println(paths.joinToString(separator = "\n"))

        // 2. Build world
        val world = World(paths)
        //println("Total walls: ${world.countTotalMaterial(Material.WALL)}")

        // 3. Run simulation
        runSimulation(world)

        // 4. Count total sand
        return world.countTotalMaterial(Material.SAND)
    }

    private fun parsePaths(): List<List<Position>> {
        return ResourceFiles.readLinesSplit(14, delimiter = " -> ")
            .map { parsePath(it) }
    }

    private fun parsePath(line: List<String>): List<Position> {
        return line.map {
            val split = it.split(",")
            Position(
                x = split[0].toInt(),
                y = split[1].toInt()
            )
        }
    }

    private fun runSimulation(world: World) {
        val source = Position(500, 0)
        var placedSand: Position? = null
        do {
            if (placedSand == source) {
                break
            }
            placedSand = world.dropSand()
            //println("Placed sand $placedSand")
        } while (placedSand != null)
    }

    fun part2(): Int {
        // 1. Parse paths
        val paths = parsePaths()
        //println(paths.joinToString(separator = "\n"))

        // 2. Build world with floor
        val world = buildWorldWithFloor(paths)
        //println("Total walls: ${world.countTotalMaterial(Material.WALL)}")

        // 3. Run simulation
        runSimulation(world)

        // 4. Count total sand
        return world.countTotalMaterial(Material.SAND)
    }

    private fun findWorldBounds(paths: List<List<Position>>): Pair<Position, Position> {
        val minX = paths.minOf { path -> path.minOf { it.x } }
        val minY = paths.minOf { path -> path.minOf { it.y } }
        val minBound = Position(minX, minY)

        val maxX = paths.maxOf { path -> path.maxOf { it.x } }
        val maxY = paths.maxOf { path -> path.maxOf { it.y } }
        val maxBound = Position(maxX, maxY)

        return Pair(minBound, maxBound)
    }

    private fun buildWorldWithFloor(paths: List<List<Position>>): World {
        val floorWidth = 1000
        val (_, max) = findWorldBounds(paths)
        val maxY = max.y + 2
        val floor: List<Position> = listOf(
            Position(0, maxY),
            Position(floorWidth, maxY)
        )
        val pathsWithFloor = paths.toMutableList().apply { add(floor) }
        return World(pathsWithFloor)
    }
}