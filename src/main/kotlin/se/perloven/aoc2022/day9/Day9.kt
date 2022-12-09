package se.perloven.aoc2022.day9

import se.perloven.aoc2022.day9.Day9.Direction.*
import se.perloven.aoc2022.util.ResourceFiles
import kotlin.math.abs

fun main() {
    println("Part 1: ${Day9.part1()}")
    println("Part 2: ${Day9.part2()}")
}

object Day9 {

    private enum class Direction {
        L, R, U, D
    }

    private data class Operation(val dir: Direction, val steps: Int)

    private data class Position(val x: Int, val y: Int)

    private data class Piece(var pos: Position, val visited: MutableSet<Position>) {
        fun move(newPos: Position) {
            this.pos = newPos
            visited.add(newPos)
        }
    }

    fun part1(): Int {
        val operations = ResourceFiles.readLinesSplit("day9/input-1.txt").map { parseOperation(it) }
        val tail = runSimulation(operations)
        return tail.visited.size
    }

    private fun parseOperation(line: List<String>): Operation {
        return Operation(
            dir = valueOf(line[0]),
            steps = line[1].toInt()
        )
    }

    private fun runSimulation(operations: List<Operation>): Piece {
        val initialPos = Position(0, 0)
        val head = Piece(initialPos, mutableSetOf(initialPos))
        val tail = Piece(initialPos, mutableSetOf(initialPos))
        operations.forEach { op ->
            repeat(op.steps) {
                updatePiece(head, op.dir)
                if (!isJoined(head, tail)) {
                    updateTail(tail, head.pos, op.dir)
                }
            }
        }

        return tail
    }

    private fun updatePiece(piece: Piece, dir: Direction) {
        val pos = piece.pos
        val newPos = when (dir) {
            L -> Position(pos.x - 1, pos.y)
            R -> Position(pos.x + 1, pos.y)
            U -> Position(pos.x, pos.y - 1)
            D -> Position(pos.x, pos.y + 1)
        }
        piece.move(newPos)
    }

    private fun isJoined(head: Piece, tail: Piece): Boolean {
        return abs(head.pos.x - tail.pos.x) <= 1 && abs(head.pos.y - tail.pos.y) <= 1
    }

    private fun updateTail(tail: Piece, headPos: Position, headDir: Direction) {
        val newTailPos = when (headDir) {
            L -> Position(headPos.x + 1, headPos.y)
            R -> Position(headPos.x - 1, headPos.y)
            U -> Position(headPos.x, headPos.y + 1)
            D -> Position(headPos.x, headPos.y - 1)
        }
        tail.move(newTailPos)
    }

    fun part2(): Int {
        return -1
    }
}