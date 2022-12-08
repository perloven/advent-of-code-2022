package se.perloven.aoc2022.day8

import se.perloven.aoc2022.util.ResourceFiles

fun main() {
    println("Part 1: ${TreetopTreeHouse.part1()}")
    println("Part 2: ${TreetopTreeHouse.part2()}")
}

object TreetopTreeHouse {

    private data class Tree(val height: Int, var visible: Boolean = false)

    fun part1(): Int {
        return parseTreeGrid()
    }

    private fun parseTreeGrid(): Int {
        val lines = ResourceFiles.readLines("day8/input-1.txt").map { line -> line.toList().map { it.digitToInt() } }
        val treeGrid = lines.map { line -> line.map { Tree(it, visible = false) } }
        println(lines)
        markVisibility(treeGrid)

        return countVisibleTrees(treeGrid)
    }

    private fun markVisibility(treeGrid: List<List<Tree>>) {
        val width = treeGrid[0].size - 1
        val height = treeGrid.size - 1
        val searchPatterns: List<Pair<IntProgression, IntProgression>> =
            listOf(
                Pair(0..height, 0..width),
                Pair(height downTo 0, 0..width),
            )
        markVertical(treeGrid, 0..height, 0..width)
        markVertical(treeGrid, height downTo 0, 0..width)
        markHorizontal(treeGrid, 0..height, 0..width)
        markHorizontal(treeGrid, 0..height, width downTo 0)
    }

    private fun markVertical(treeGrid: List<List<Tree>>, heights: IntProgression, widths: IntProgression) {
        val highest = IntArray(100 ) { -1 }
        for (h in heights) {
            for (w in widths) {
                val tree = treeGrid[h][w]
                if (tree.height > highest[w]) {
                    tree.visible = true
                    highest[w] = tree.height
                }
            }
        }
    }

    private fun markHorizontal(treeGrid: List<List<Tree>>, heights: IntProgression, widths: IntProgression) {
        val highest = IntArray(100 ) { -1 }
        for (w in widths) {
            for (h in heights) {
                val tree = treeGrid[h][w]
                if (tree.height > highest[h]) {
                    tree.visible = true
                    highest[h] = tree.height
                }
            }
        }
    }

    private fun countVisibleTrees(treeGrid: List<List<Tree>>): Int {
        return treeGrid.sumOf { line -> line.count { it.visible } }
    }

    fun part2(): Int {
        return -1
    }
}