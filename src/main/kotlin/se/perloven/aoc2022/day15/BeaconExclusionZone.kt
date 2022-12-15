package se.perloven.aoc2022.day15

import se.perloven.aoc2022.util.Position
import se.perloven.aoc2022.util.ResourceFiles
import kotlin.math.abs

fun main() {
    println("Part 1: ${BeaconExclusionZone.part1()}")
    println("Part 2: ${BeaconExclusionZone.part2()}")
}

object BeaconExclusionZone {

    private data class Sensor(val pos: Position, val closestBeaconPos: Position)

    private class World(sensors: List<Sensor>) {
        private val excludedRanges: MutableList<IntRange> = mutableListOf()

        init {
            sensors.forEach {
                // println("registering sensor $it")
                registerSensor(it)
            }
        }

        private fun registerSensor(sensor: Sensor) {
            val distance = calcManhattanDistance(sensor.pos, sensor.closestBeaconPos)
            if (!crossesTheLine(sensor.pos, distance)) {
                return
            }

            val rangeWidth = distance - abs(2_000_000 - sensor.pos.y)
            val range = (sensor.pos.x - rangeWidth)..(sensor.pos.x + rangeWidth)
            excludedRanges.add(range)
        }

        private fun crossesTheLine(sonarPos: Position, distanceToBeacon: Int): Boolean {
            return 2_000_000 - abs(sonarPos.y) < distanceToBeacon
        }

        fun calcNoBeaconPositions(): Int {
            val xs = mutableSetOf<Int>()
            excludedRanges.forEach { xs.addAll(it.toList()) }
            return xs.size - 1
        }
    }

    fun part1(): Int {
        val lines = ResourceFiles.readLinesSplit(15)
        val sensors = parseSensors(lines)

        val world = World(sensors)
        println(sensors.joinToString(separator = "\n") {
            "$it : ${
                calcManhattanDistance(
                    it.pos,
                    it.closestBeaconPos
                )
            }"
        })
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

    private data class Sensor2(val pos: Position, val range: Int)

    fun part2(): Long {
        val lines = ResourceFiles.readLinesSplit(15)
        val sensors2 = parseSensors2(lines)

        println(sensors2.joinToString(separator = "\n"))
        val beaconPos = findBeaconPos(sensors2)

        return calcTuningFrequency(beaconPos)
    }

    private fun parseSensors2(lines: List<List<String>>): List<Sensor2> {
        return parseSensors(lines)
            .map {
                Sensor2(
                    pos = it.pos,
                    range = calcManhattanDistance(it.pos, it.closestBeaconPos)
                )
            }
    }

    private fun findBeaconPos(sensors2: List<Sensor2>): Position {
        val coordinateRange = 0..4_000_000
        for (x in coordinateRange) {
            for (y in coordinateRange) {
                val curPos = Position(x, y)
                if (isOutsideRange(curPos, sensors2)) {
                    return curPos
                } else {
                    println("Beacon not at $curPos")
                }
            }
        }

        throw IllegalStateException("Beacon not found")
    }

    private fun isOutsideRange(pos: Position, sensors2: List<Sensor2>): Boolean {
        return sensors2.all { !isInRange(pos, it) }
    }

    private fun isInRange(pos: Position, sensor2: Sensor2): Boolean {
        return calcManhattanDistance(pos, sensor2.pos) <= sensor2.range
    }

    private fun calcTuningFrequency(beaconPos: Position): Long {
        return beaconPos.x * 4_000_000L + beaconPos.y
    }
}