package se.perloven.aoc2022.day10

import se.perloven.aoc2022.util.ResourceFiles
import java.util.*

fun main() {
    println("Part 1: ${Day10.part1()}")
    println("Part 2: ${Day10.part2()}")
}

object Day10 {

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

            while (processingInstruction.remainingCycles > 0) {
                processingInstruction.remainingCycles--

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

    fun part2(): Int {
        return -1
    }
}