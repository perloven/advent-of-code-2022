package se.perloven.aoc2022.day16

import se.perloven.aoc2022.util.ResourceFiles
import kotlin.math.max

fun main() {
    println("Part 1: ${ProboscidaeVolcanium.part1()}")
    println("Part 2: ${ProboscidaeVolcanium.part2()}")
}

object ProboscidaeVolcanium {

    private data class Valve(val name: String, val flowRate: Int, var isOpen: Boolean = false,
                             var connectsTo: Set<Valve> = emptySet()) {
        override fun toString(): String {
            return "Valve(name=$name, flowRate=$flowRate, connectsTo=${
                connectsTo.joinToString(prefix = "[", postfix = "]") { it.name }})"
        }

        override fun hashCode(): Int {
            return name.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            return other is Valve && name == other.name && flowRate == other.flowRate
        }
    }

    // 2431 is too high
    // 2331 is also too high
    // 2218 is also too high
    // 2140 is wrong
    fun part1(): Long {
        val graph = loadGraph()
        //println(graph.joinToString("\n"))
        //val totalPossibleFlowRate = graph.sumOf { it.flowRate }
        //println("Total possible flow rate: $totalPossibleFlowRate")
        val startValve = graph.first { it.name == "AA" }

        println("Looking for maximum released pressure...")
        return -1
        return findMaxReleasedPressure(1, 0, 0, emptySet(), startValve)
    }

    private fun loadGraph(): List<Valve> {
        val lines = ResourceFiles.readLines(16)

        val valvesWithConnections = lines.map { parseValveWithConnections(it) }

        return setConnections(valvesWithConnections)
    }

    private fun parseValveWithConnections(line: String): Pair<Valve, List<String>> {
        val splitLine = line
            .replace(";", "")
            .replace(",", "")
            .split(" ")
        return Pair(
            first = Valve(
                name = splitLine[1],
                flowRate = splitLine[4].removePrefix("rate=").toInt()
            ),
            second = splitLine.drop(9)
        )
    }

    private fun setConnections(valvesWithConnections: List<Pair<Valve, List<String>>>): List<Valve> {
        valvesWithConnections.forEach { pair ->
            pair.first.connectsTo = pair.second.map { findValveByName(it, valvesWithConnections) }.toSet()
        }
        return valvesWithConnections.map { it.first }
    }

    private fun findValveByName(name: String, valvesWithConnections: List<Pair<Valve, List<String>>>): Valve {
        return valvesWithConnections
            .map { it.first }
            .firstOrNull { it.name == name } ?: throw IllegalStateException("Found no valve with name $name")
    }

    // trying all combinations will lead to SO error. Each valve has more than 2 options on average.
    // lower bound would be options^minutes = 2^30 stack frames.
    // Need to come up with some way of dismissing entire paths after a while.
    private fun findMaxReleasedPressure(minute: Int, flowRate: Int, pressureReleased: Long, openedValves: Set<Valve>,
                                        currentValve: Valve): Long {
        if (minute > 30) {
            return pressureReleased
        }
        /*
        if (openedValves.size >= 15) {
            println("All valves open")
            return pressureReleased + (30 - minute) * flowRate
        }

         */

        if (minute > 5 && (pressureReleased <= 0 || flowRate <= 0)) {
            return -105
        }
        /*
        if (minute > 10 && (pressureReleased < 10 || flowRate < 5)) {
            return -110
        }
        if (minute > 15 && (pressureReleased < 50 || flowRate < 15)) {
            return -115
        }


         */
        if (minute > 10 && flowRate < 30) {
            return -110
        }
        if (minute > 20 && (pressureReleased <= 750 || flowRate < 150)) {
            return -120
        }
        /*
        if (minute > 25 && (pressureReleased < 1500)) {
            return -125
        }

         */

        val nextMinute = minute + 1
        val nextTotalPressureReleased = pressureReleased + flowRate
        return currentValve.connectsTo.maxOf {
            if (it in openedValves && it.flowRate == 0) {
                findMaxReleasedPressure(nextMinute, flowRate, nextTotalPressureReleased, openedValves, it)
            } else {
                val pressureIfOpening = openValve(nextMinute, flowRate, pressureReleased, openedValves, it)
                val pressureIfTraversing = findMaxReleasedPressure(nextMinute, flowRate, nextTotalPressureReleased, openedValves, it)
                max(pressureIfOpening, pressureIfTraversing)
            }
        }
    }

    private fun openValve(minutes: Int, flowRate: Int, pressureReleased: Long, openedValves: Set<Valve>, currentValve: Valve): Long {
        return currentValve.connectsTo.maxOf {
            findMaxReleasedPressure(
                minutes + 1,
                flowRate = flowRate + currentValve.flowRate,
                pressureReleased = pressureReleased + flowRate,
                openedValves = openedValves + currentValve,
                currentValve = it
            )
        }
    }

    fun part2(): Int {
        return -2
    }
}