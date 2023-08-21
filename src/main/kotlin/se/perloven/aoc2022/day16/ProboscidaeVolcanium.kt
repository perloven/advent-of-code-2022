package se.perloven.aoc2022.day16

import se.perloven.aoc2022.util.ResourceFiles
import java.util.*

fun main() {
    println("Part 1: ${ProboscidaeVolcanium.part1()}")
    println("Part 2: ${ProboscidaeVolcanium.part2()}")
}

object ProboscidaeVolcanium {

    // 2431 is too high
    // 2331 is also too high
    // 2218 is also too high
    // 2140 is wrong

    // 1392 is wrong
    // 1492 is wrong
    fun part1(): Long {
        val valves = loadValves()

        val graph = Graph(valves)

        //println("Graph: $graph")
        //println("Max released pressure: ${graph.findMaxReleasedPressure()}")

        //println("Looking for maximum released pressure...")
        return graph.findMaxReleasedPressure()
    }

    private fun loadValves(): List<Valve> {
        val lines = ResourceFiles.readLines(16)

        val valvesWithConnections = lines.map(::parseValveWithConnections)

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
            pair.first.edges = pair.second
                .map { Edge(cost = 1, to = findValveByName(it, valvesWithConnections)) }
                .toSet()
        }
        return valvesWithConnections.map { it.first }
    }

    private fun findValveByName(name: String, valvesWithConnections: List<Pair<Valve, List<String>>>): Valve {
        return valvesWithConnections
            .map { it.first }
            .firstOrNull { it.name == name } ?: throw IllegalStateException("Found no valve with name $name")
    }

    fun part2(): Int {
        return -2
    }
}
