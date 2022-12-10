package se.perloven.aoc2022.day10

import se.perloven.aoc2022.util.ResourceFiles
import java.util.*
import kotlin.math.abs

fun main() {
    println("Part 1: ${CathodeRayTube.part1()}")
    println("Part 2: ${CathodeRayTube.part2()}")
}

object CathodeRayTube {

    private sealed interface Instruction
    private object NoopInstruction : Instruction
    private class AddInstruction(val value: Int) : Instruction

    private data class ProcessingInstruction(val value: Int, var remainingCycles: Int)

    fun part1(): Int {
        val lines = ResourceFiles.readLinesSplit(10)
        val instructions = parseInstructions(lines)
        return sumSignalStrengths(instructions)
    }

    private fun parseInstructions(lines: List<List<String>>): List<Instruction> {
        return lines.map { parseInstruction(it) }
    }

    private fun parseInstruction(line: List<String>) : Instruction {
        return when (val op = line[0]) {
            "noop" -> NoopInstruction
            "addx" -> AddInstruction(value = line[1].toInt())
            else -> throw IllegalArgumentException("Unrecognized instruction $op")
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
            processingInstruction = when (instruction) {
                is NoopInstruction -> ProcessingInstruction(0, 1)
                is AddInstruction -> ProcessingInstruction(instruction.value, 2)
            }

            while (processingInstruction.remainingCycles-- > 0) {

                if (cycle in relevantCycles) {
                    sumOfSignalStrengths += cycle * registerValue
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
        while (instructionStack.isNotEmpty()) {
            val instruction = instructionStack.pop()
            processingInstruction = when (instruction) {
                is NoopInstruction -> ProcessingInstruction(0, 1)
                is AddInstruction -> ProcessingInstruction(instruction.value, 2)
            }

            while (processingInstruction.remainingCycles-- > 0) {
                val x = cycle % 40
                val y = cycle / 40
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