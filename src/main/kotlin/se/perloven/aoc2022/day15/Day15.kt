package se.perloven.aoc2022.day15

import se.perloven.aoc2022.util.Position
import se.perloven.aoc2022.util.ResourceFiles
import kotlin.math.abs

fun main() {
    println("Part 1: ${Day15.part1()}")
    println("Part 2: ${Day15.part2()}")
}

object Day15 {

    private data class Sensor(val pos: Position, val closestBeaconPos: Position)

    private enum class Material {
        SENSOR,
        BEACON,
        NO_BEACON
    }

    private class World(sensors: List<Sensor>) {
        private val scanResult: MutableMap<Position, Material> = mutableMapOf()

        init {
            sensors.forEach {
                println("registering sensor $it")
                registerSensor(it)
            }
        }

        private fun registerSensor(sensor: Sensor) {
            scanResult[sensor.pos] = Material.SENSOR
            val distance = calcManhattanDistance(sensor.pos, sensor.closestBeaconPos)
            val xRange = (sensor.pos.x - distance)..(sensor.pos.x + distance)
            val yRange = (sensor.pos.y - distance)..(sensor.pos.y + distance)
            for (x in xRange) {
                for (y in yRange) {
                    val curPos = Position(x, y)
                    if (calcManhattanDistance(sensor.pos, curPos) > distance) {
                        continue
                    }

                    val material: Material? = scanResult[curPos]
                    if (material == null || material == Material.SENSOR || material == Material.BEACON) {
                        continue
                    }

                    scanResult[curPos] = Material.NO_BEACON
                }
            }
        }

        fun calcNoBeaconPositions(): Int {
            return scanResult
                .filterKeys { it.y == 2_000_000 }
                .filterValues { it != Material.BEACON }
                .count()
        }
    }

    fun part1(): Int {
        val lines = ResourceFiles.readLinesSplit(15)
        val sensors = parseSensors(lines)

        val world = World(sensors)
        println(sensors.joinToString(separator = "\n") { "$it : ${calcManhattanDistance(it.pos, it.closestBeaconPos)}"})
        return world.calcNoBeaconPositions()
    }

    private fun parseSensors(lines: List<List<String>>): List<Sensor> {
        return lines.map { parseSensor(it) }
    }

    private fun parseSensor(line: List<String>): Sensor {
        return Sensor(
            pos = Position(
                x = line[2].drop(2).dropLast(1).toInt(),
                y = line[3].drop(2).dropLast(1).toInt()
            ),
            closestBeaconPos = Position(
                x = line[8].drop(2).dropLast(1).toInt(),
                y = line[9].drop(2).toInt()
            )
        )
    }

    private fun calcManhattanDistance(p1: Position, p2: Position): Int {
        return abs(p1.x - p2.x) + abs(p1.y - p2.y)
    }

    fun part2(): Int {
        return -2
    }
}