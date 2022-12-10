package se.perloven.aoc2022.day10

import se.perloven.aoc2022.util.ResourceFiles
import java.util.*
import kotlin.math.abs

fun main() {
    println("Part 1: ${CathodeRayTube.part1()}")
    println("Part 2: ${CathodeRayTube.part2()}")
}

object CathodeRayTube {

    private enum class Operation {
        NOOP,
        ADDX
    }

    private data class Instruction(val op: Operation, val value: Int? = null)

    private data class ProcessingInstruction(val value: Int, var remainingCycles: Int)

    fun part1(): Int {
        val lines = ResourceFiles.readLinesSplit(10)
        val instructions = parseInstructions(lines)
        return sumSignalStrengths(instructions)
    }

    private fun parseInstructions(lines: List<List<String>>): List<Instruction> {
        return lines.map { parseOperation(it) }
    }

    private fun parseOperation(line: List<String>) : Instruction {
        val op = line[0]
        return if (op == "noop") {
            Instruction(Operation.NOOP)
        } else {
            Instruction(Operation.ADDX, value = line[1].toInt())
        }
    }

    private fun sumSignalStrengths(instructions: List<Instruction>): Int {
        val instructionStack: Stack<Instruction> = Stack<Instruction>().apply {
            instructions.reversed().forEach { push(it) }
        }
        val relevantCycles = setOf(20, 60, 100, 140, 180, 220)
        var sumOfSignalStrengths = 0

        var cycle = 1
        var registerValue = 1
        var processingInstruction: ProcessingInstruction
        while (instructionStack.isNotEmpty()) {
            val instruction = instructionStack.pop()
            processingInstruction = if (instruction.op == Operation.NOOP) {
                ProcessingInstruction(0, 1)
            } else {
                ProcessingInstruction(instruction.value!!, 2)
            }

            while (processingInstruction.remainingCycles-- > 0) {

                if (cycle in relevantCycles) {
                    sumOfSignalStrengths += cycle * registerValue
                    //println("Cycle $cycle, registerValue $registerValue, sumOfStrengths $sumOfSignalStrengths")
                }
                cycle++
            }
            registerValue += processingInstruction.value
        }

        return sumOfSignalStrengths
    }

    fun part2(): String {
        val lines = ResourceFiles.readLinesSplit(10)
        val instructions = parseInstructions(lines)
        val drawnImage = drawImage(instructions)
        return drawnImage.joinToString(prefix = "\n", separator = "\n") { it.joinToString(separator = "") }
    }


    private fun drawImage(instructions: List<Instruction>): Array<Array<String>> {
        val drawnImage: Array<Array<String>> = Array(6) { Array(40) { "." } }
        val instructionStack: Stack<Instruction> = Stack<Instruction>().apply {
            instructions.reversed().forEach { push(it) }
        }

        var cycle = 0
        var registerValue = 1
        var processingInstruction: ProcessingInstruction
        var x = 0
        var y = 0
        while (instructionStack.isNotEmpty()) {
            val instruction = instructionStack.pop()
            processingInstruction = if (instruction.op == Operation.NOOP) {
                ProcessingInstruction(0, 1)
            } else {
                ProcessingInstruction(instruction.value!!, 2)
            }

            while (processingInstruction.remainingCycles-- > 0) {
                x = cycle % 40
                y = cycle / 40
                val shouldDraw = abs(registerValue - x) < 2
                if (shouldDraw) {
                    drawnImage[y][x] = "#"
                }
                cycle++
            }
            registerValue += processingInstruction.value
        }

        return drawnImage
    }
}