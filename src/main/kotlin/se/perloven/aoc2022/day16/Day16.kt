package se.perloven.aoc2022.day16

import se.perloven.aoc2022.util.ResourceFiles
import kotlin.math.max

fun main() {
    println("Part 1: ${Day16.part1()}")
    println("Part 2: ${Day16.part2()}")
}

object Day16 {

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
    fun part1(): Long {
        val graph = loadGraph()
        println(graph.joinToString("\n"))
        val totalPossibleFlowRate = graph.sumOf { it.flowRate }
        println("Total possible flow rate: $totalPossibleFlowRate")
        return -1
        // findMaxFlowRate(30, 0, 0, emptySet(), graph.first { it.name == "AA" })
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
    private fun findMaxFlowRate(remainingMinutes: Int, flowRate: Int, pressureReleased: Long, openedValves: Set<Valve>,
                                currentValve: Valve): Long {
        if (remainingMinutes <= 0) {
            val totalPressureReleased = pressureReleased + flowRate
            println("Time out with total pressure released: $totalPressureReleased (flowRate $flowRate)")
            return totalPressureReleased
        }
        if (remainingMinutes < 25 && (pressureReleased <= 0 || flowRate <= 0)) {
            return -100
        }
        if (remainingMinutes < 16 && (pressureReleased <= 200 || flowRate < 100)) {
            return -101
        }
        return currentValve.connectsTo.maxOf {
            if (it in openedValves || it.flowRate == 0) {
                findMaxFlowRate(remainingMinutes - 1, flowRate, pressureReleased + flowRate, openedValves, it)
            } else {
                max(
                    findMaxFlowRate(remainingMinutes - 1, flowRate, pressureReleased + flowRate, openedValves, it),
                    openValve(remainingMinutes - 1, flowRate, pressureReleased + flowRate, openedValves, it)
                )
            }
        }
    }


    private fun openValve(remainingMinutes: Int, flowRate: Int, pressureReleased: Long, openedValves: Set<Valve>, currentValve: Valve): Long {
        if (remainingMinutes <= 0) {
            val totalPressureReleased = pressureReleased + flowRate
            println("Time out with total pressure released: $totalPressureReleased (flowRate $flowRate)")
            return totalPressureReleased
        }
        if (remainingMinutes < 20 && (pressureReleased <= 0 || flowRate <= 0)) {
            return -100
        }
        if (remainingMinutes < 10 && pressureReleased <= 200) {
            return -101
        }

        return currentValve.connectsTo.maxOf {
            findMaxFlowRate(
                remainingMinutes = remainingMinutes - 1,
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