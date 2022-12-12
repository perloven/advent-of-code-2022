package se.perloven.aoc2022.day12

import se.perloven.aoc2022.util.Position
import se.perloven.aoc2022.util.ResourceFiles
import java.util.*

fun main() {
    println("Part 1: ${HillClimbingAlgorithm.part1()}")
    println("Part 2: ${HillClimbingAlgorithm.part2()}")
}

object HillClimbingAlgorithm {

    private data class Node(
        val height: Int, val pos: Position, val isStart: Boolean = false, val isTarget: Boolean = false,
        var visited: Boolean = false, val adjacent: MutableList<Node> = mutableListOf(), var parent: Node? = null
    ) {
        override fun toString(): String {
            return "Node[height=$height, visited=$visited, adjacents=${adjacent.size}]"
        }
    }

    fun part1(): Int {
        val graph = createGraph()
        val sNode = graph[20][0]
        val targetNode = traceShortestPath(sNode)
        return countShortestPath(targetNode!!)
    }

    private fun createGraph(): Array<Array<Node>> {
        val lines = ResourceFiles.readLines(12)
        return parseElevations(lines).also {
            connectGridNodes(it)
        }
    }

    private fun parseElevations(lines: List<String>): Array<Array<Node>> {
        val grid = Array(lines.size) { Array(lines.first().length) { Node(height = -1, Position(-1, -1)) } }
        for ((y, line) in lines.withIndex()) {
            for ((x, char) in line.toList().withIndex()) {
                val pos = Position(x, y)
                val node = if (char == 'S') {
                    Node(height = toHeight('a'), pos = pos, isStart = true)
                } else if (char == 'E') {
                    Node(height = toHeight('z'), pos = pos, isTarget = true)
                } else {
                    Node(height = toHeight(char), pos = pos)
                }
                grid[y][x] = node
            }
        }
        return grid
    }

    private fun toHeight(char: Char): Int {
        require(char.code in 'a'.code..'z'.code)
        return char.code - 'a'.code
    }

    private fun connectGridNodes(grid: Array<Array<Node>>) {
        for ((y, row) in grid.withIndex()) {
            for ((x, node) in row.withIndex()) {
                val adjacentNodes = adjacentNodes(grid, Position(x, y))
                val visitableAdjacentNodes = adjacentNodes.filter { node.height + 1 >= it.height }
                node.adjacent.addAll(visitableAdjacentNodes)
            }
        }
    }

    private fun adjacentNodes(grid: Array<Array<Node>>, pos: Position): List<Node> {
        val heights = grid.indices
        val widths = grid[0].indices
        val adjacentNodes = mutableListOf<Node>()
        if (pos.x - 1 in widths) {
            adjacentNodes.add(grid[pos.y][pos.x - 1])
        }
        if (pos.x + 1 in widths) {
            adjacentNodes.add(grid[pos.y][pos.x + 1])
        }
        if (pos.y + 1 in heights) {
            adjacentNodes.add(grid[pos.y + 1][pos.x])
        }
        if (pos.y - 1 in heights) {
            adjacentNodes.add(grid[pos.y - 1][pos.x])
        }
        return adjacentNodes
    }

    private fun traceShortestPath(startNode: Node): Node? {
        val queue: Queue<Node> = LinkedList()
        startNode.visited = true
        queue.add(startNode)
        while (queue.isNotEmpty()) {
            val node = queue.poll()
            if (node.isTarget) {
                return node
            }
            node.adjacent
                .filter { !it.visited }
                .forEach {
                    it.visited = true
                    it.parent = node
                    queue.add(it)
                }
        }

        return null
    }

    private fun countShortestPath(targetNode: Node): Int {
        var count = 0
        var curNode: Node? = targetNode.parent
        while (curNode != null) {
            curNode = curNode.parent
            count++
        }

        return count
    }

    fun part2(): Int {
        val graph = createGraph()
        val startNodes = findAllA(graph)
        return startNodes.minOf { calcShortestPathToTarget(graph, it) }
    }

    private fun calcShortestPathToTarget(graph: Array<Array<Node>>, startNode: Node): Int {
        clearVisited(graph)
        val targetNode = traceShortestPath(startNode)
        return targetNode?.let { countShortestPath(it) } ?: Int.MAX_VALUE
    }

    private fun findAllA(graph: Array<Array<Node>>): List<Node> {
        val allAs = mutableListOf<Node>()
        for (row in graph) {
            for (node in row) {
                if (node.height == 0) {
                    allAs.add(node)
                }
            }
        }

        return allAs
    }

    private fun clearVisited(graph: Array<Array<Node>>) {
        for (row in graph) {
            for (node in row) {
                node.visited = false
                node.parent = null
            }
        }
    }
}