package se.perloven.aoc2022.day8

import se.perloven.aoc2022.util.ResourceFiles
import kotlin.math.max
import kotlin.math.min

fun main() {
    println("Part 1: ${TreetopTreeHouse.part1()}")
    println("Part 2: ${TreetopTreeHouse.part2()}")
}

object TreetopTreeHouse {

    private data class Tree(val height: Int, var visible: Boolean = false)

    fun part1(): Int {
        val treeGrid = parseTreeGrid()
        markVisibility(treeGrid)
        return countVisibleTrees(treeGrid)
    }

    private fun parseTreeGrid(): List<List<Tree>> {
        val lines = ResourceFiles.readLines(8).map { line -> line.toList().map { it.digitToInt() } }
        return lines.map { line -> line.map { Tree(it, visible = false) } }
    }

    private fun markVisibility(treeGrid: List<List<Tree>>) {
        val width = treeGrid[0].size - 1
        val height = treeGrid.size - 1
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
        val treeGrid = parseTreeGrid()
        var max = 0
        for (y in treeGrid.indices) {
            for (x in treeGrid[y].indices) {
                val scenicScore = calculateScenicScore(treeGrid, x, y)
                if (scenicScore > max) {
                    max = scenicScore
                }
            }
        }

        return max
    }

    private fun calculateScenicScore(treeGrid: List<List<Tree>>, x: Int, y: Int): Int {
        val height = treeGrid.size - 1
        val width = treeGrid[0].size - 1
        val tree = treeGrid[x][y]
        val leftScore = calculateHorizontalScore(treeGrid, tree.height, max(0, x - 1) downTo 0, y)
        val rightScore = calculateHorizontalScore(treeGrid, tree.height, min(width, x + 1)..width, y)
        val upScore = calculateVerticalScore(treeGrid, tree.height, x, max(0, y - 1) downTo 0)
        val downScore = calculateVerticalScore(treeGrid, tree.height, x, min(height, y + 1)..height)
        return leftScore * rightScore * upScore * downScore
    }

    private fun calculateVerticalScore(treeGrid: List<List<Tree>>, maxHeight: Int, x: Int, ys: IntProgression): Int {
        var trees = 0
        for (y in ys) {
            trees++
            val testTree = treeGrid[x][y]

            if (testTree.height >= maxHeight) {
                return trees
            }
        }

        return trees
    }

    private fun calculateHorizontalScore(treeGrid: List<List<Tree>>, maxHeight: Int, xs: IntProgression, y: Int): Int {
        var trees = 0
        for (x in xs) {
            trees++
            val testTree = treeGrid[x][y]

            if (testTree.height >= maxHeight) {
                return trees
            }
        }

        return trees
    }
}