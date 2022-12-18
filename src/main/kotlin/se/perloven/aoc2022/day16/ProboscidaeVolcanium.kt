package se.perloven.aoc2022.day16

import se.perloven.aoc2022.util.ResourceFiles
import java.util.*

fun main() {
    println("Part 1: ${ProboscidaeVolcanium.part1()}")
    println("Part 2: ${ProboscidaeVolcanium.part2()}")
}

object ProboscidaeVolcanium {

    private class Graph(private val initialValves: List<Valve>) {
        companion object {
            private const val TOTAL_MINUTES = 30
        }
        private val optimizedValves: List<Valve> = optimize()

        private fun optimize(): List<Valve> {
            val toValves = initialValves.filter { hasFlow(it) }
            val fromValves = toValves + initialValves.first { isStart(it) }
            val directEdgeValves = fromValves
                .map { from ->
                    val edges = toValves
                        .filter { iv -> iv.name != from.name }
                        .map { to ->
                            Edge(
                                cost = distanceBetween(from, to),
                                toName = to.name
                            )
                        }
                        .toSet()
                    Valve(
                        name = from.name,
                        flowRate = from.flowRate,
                        edges = edges,
                    )
                }
            //println("directEdgeValues: ${directEdgeValves.joinToString(separator = "\n")}")
            directEdgeValves.forEach { valve ->
                valve.edges.forEach { edge -> edge.to = directEdgeValves.first { it.name == edge.toName } }
            }

            check(directEdgeValves.count { it.flowRate == 0 } == 1)
            check(directEdgeValves.first { it.flowRate == 0 }.name == "AA")
            check(directEdgeValves.all { valve -> valve.edges.all { it.to.flowRate > 0 } })
            return directEdgeValves
        }

        private fun isStart(valve: Valve): Boolean {
            return valve.name == "AA"
        }

        private fun hasFlow(valve: Valve): Boolean {
            return valve.flowRate > 0
        }

        private fun distanceBetween(from: Valve, to: Valve): Int {
            val foundTo = bfs(from, to)
            checkNotNull(foundTo) { "Valve ${to.name} could not be found from valve ${from.name}" }

            var distance = 0
            var curValve: Valve? = foundTo.parent
            while (curValve != null) {
                distance++
                curValve = curValve.parent
            }
            resetValves()

            return distance
        }

        private fun bfs(from: Valve, to: Valve): Valve? {
            val queue: Queue<Valve> = LinkedList()
            from.visited = true
            queue.add(from)
            while (queue.isNotEmpty()) {
                val valve = queue.poll()
                if (valve == to) {
                    return valve
                }
                valve.edges
                    .filter { !it.to.visited }
                    .forEach {
                        it.to.visited = true
                        it.to.parent = valve
                        queue.add(it.to)
                    }
            }

            return null
        }

        private fun resetValves() {
            initialValves.forEach {
                it.parent = null
                it.visited = false
            }
        }

        fun findMaxReleasedPressure(): Long {
            val startValve = optimizedValves.first { isStart(it) }
            return findMaxReleasedPressure(1, 0, 0, emptySet(), startValve)
        }

        private fun findMaxReleasedPressure(minute: Int, flowRate: Int, pressureReleased: Long,
                                            openedValves: Set<Valve>, currentValve: Valve): Long {
            if (minute > TOTAL_MINUTES) {
                return pressureReleased
            }

            val viableEdges = currentValve.edges
                .filter { it.to.flowRate > 0 }
                .filter { it.to !in openedValves }
                .filter { it.cost + 1 <= TOTAL_MINUTES - minute }
            if (viableEdges.isEmpty()) {
                return pressureReleased + (TOTAL_MINUTES - minute + 1) * flowRate
            }

            return viableEdges
                .maxOf {
                    val totalCost = it.cost + 1 // it.cost is traversal, and then +1 to turn valve on
                    val newPressureTotal = pressureReleased + (totalCost * flowRate)
                    val newOpenedValves = openedValves + it.to
                    check(it.to.flowRate > 0) { "Visiting ${it.to.name} even though it has no flow" }
                    findMaxReleasedPressure(minute + totalCost, flowRate + it.to.flowRate, newPressureTotal, newOpenedValves, it.to)
                }
        }

        override fun toString(): String {
            return optimizedValves.joinToString(separator = "\n")
        }
    }

    private data class Edge(val cost: Int, val toName: String = "", var to: Valve = PLACEHOLDER) {
        companion object {
            private val PLACEHOLDER: Valve = Valve(name = "placeholder", flowRate = 0)
        }
        override fun toString(): String {
            return "--$cost--> ${to.name}"
        }
    }

    private data class Valve(
        val name: String, val flowRate: Int, var edges: Set<Edge> = emptySet(),
        var visited: Boolean = false, var parent: Valve? = null
    ) {

        override fun toString(): String {
            return "Valve(name=$name, flowRate=$flowRate, edges=${
                edges.joinToString(prefix = "[", postfix = "]")
            })"
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