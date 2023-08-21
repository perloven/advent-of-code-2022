package se.perloven.aoc2022.day16

import java.util.*

class Graph(private val initialValves: List<Valve>) {
    companion object {
        private const val TOTAL_MINUTES = 30
    }
    private val optimizedValves: List<Valve> = optimize()

    private fun optimize(): List<Valve> {
        val toValves = initialValves.filter(Valve::hasFlow)
        val fromValves = toValves + initialValves.first(Valve::isStart)
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
        val startValve = optimizedValves.first(Valve::isStart)
        return findMaxReleasedPressure(1, 0, 0, emptySet(), startValve)
    }

    private fun findMaxReleasedPressure(minute: Int, flowRate: Int, pressureReleased: Long,
                                        openedValves: Set<Valve>, currentValve: Valve
    ): Long {
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
