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

    private data class Piece(
        var pos: Position,
        val visited: MutableSet<Position>,
        var trailing: Piece? = null,
        val id: Int = 0
    ) {
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
                    updateTail(tail, head.pos)
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

    private fun updateTail(tail: Piece, headPos: Position) {
        val tailPos = tail.pos
        val shouldSameX = abs(tailPos.x - headPos.x) >= 2
        val shouldSameY = abs(tailPos.y - headPos.y) >= 2
        val shouldMoveDiagonally = shouldSameX && shouldSameY
        val newTailPos: Position =
            if (shouldMoveDiagonally) {
                if (tailPos.x < headPos.x) {
                    if (tailPos.y < headPos.y) {
                        Position(headPos.x - 1, headPos.y - 1)
                    } else {
                        Position(headPos.x - 1, headPos.y + 1)
                    }
                } else {
                    if (tailPos.y < headPos.y) {
                        Position(headPos.x + 1, headPos.y - 1)
                    } else {
                        Position(headPos.x + 1, headPos.y + 1)
                    }
                }
            } else if (shouldSameX) {
                if (tailPos.x < headPos.x) {
                    Position(headPos.x - 1, headPos.y)
                } else {
                    Position(headPos.x + 1, headPos.y)
                }
            } else if (shouldSameY) {
                if (tailPos.y < headPos.y) {
                    Position(headPos.x, headPos.y - 1)
                } else {
                    Position(headPos.x, headPos.y + 1)
                }
            } else {
                throw IllegalStateException("Illegal move - tail $tail, headPos $headPos")
            }
        tail.move(newTailPos)
    }

    fun part2(): Int {
        val operations = ResourceFiles.readLinesSplit("day9/input-1.txt").map { parseOperation(it) }
        val tail = runSimulationTen(operations)
        println("Tail id ${tail.id}")
        return tail.visited.size
    }

    private fun runSimulationTen(operations: List<Operation>): Piece {
        val head = initTenPieces()
        operations.forEach { op ->
            repeat(op.steps) {

                updatePiece(head, op.dir)
                var curHead = head
                var curTail = head.trailing
                while (curTail != null) {
                    if (!isJoined(curHead, curTail)) {
                        updateTail(curTail, curHead.pos)
                    }
                    curHead = curTail
                    curTail = curTail.trailing
                }
            }
        }


        return findTail(head)
    }

    private fun initTenPieces(): Piece {
        val initialPos = Position(0, 0)
        val head = Piece(initialPos, mutableSetOf(initialPos), id = 1)
        var piece = head
        for (i in 2..10) {
            val nextPiece = Piece(initialPos, mutableSetOf(initialPos), id = i)
            piece.trailing = nextPiece
            piece = nextPiece
        }
        return head
    }

    private fun findTail(head: Piece): Piece {
        var cur: Piece = head
        while (cur.trailing != null) {
            cur = cur.trailing!!
        }
        return cur
    }
}