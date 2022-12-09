package se.perloven.aoc2022.day5

import se.perloven.aoc2022.util.ResourceFiles
import java.util.*

fun main() {
    println("Part 1: ${SupplyStacks.part1()}")
    println("Part 2: ${SupplyStacks.part2()}")
}

object SupplyStacks {

    private data class Operation(val qty: Int, val source: Int, val dest: Int)

    private fun initialStacks(): List<Stack<String>> {
        return listOf(
            Stack<String>().apply {
                addAll(listOf("M", "J", "C", "B", "F", "R", "L", "H"))
            },
            Stack<String>().apply {
                addAll(listOf("Z", "C", "D"))
            },
            Stack<String>().apply {
                addAll(listOf("H", "J", "F", "C", "N", "G", "W"))
            },
            Stack<String>().apply {
                addAll(listOf("P", "J", "D", "M", "T", "S", "B"))
            },
            Stack<String>().apply {
                addAll(listOf("N", "C", "D", "R", "J"))
            },
            Stack<String>().apply {
                addAll(listOf("W", "L", "D", "Q", "P", "J", "G", "Z"))
            },
            Stack<String>().apply {
                addAll(listOf("P", "Z", "T", "F", "R", "H"))
            },
            Stack<String>().apply {
                addAll(listOf("L", "V", "M", "G"))
            },
            Stack<String>().apply {
                addAll(listOf("C", "B", "G", "P", "F", "Q", "R", "J"))
            }
        )
    }

    private fun operations(): List<Operation> {
        val lines = ResourceFiles.readLines(5)
        val operationLines = lines.drop(10)
        return operationLines.map { parseOperation(it) }
    }

    private fun parseOperation(line: String): Operation {
        val parts = line.split(" ")
        return Operation(
            qty = parts[1].toInt(),
            source = parts[3].toInt(),
            dest = parts[5].toInt()
        )
    }

    fun part1(): String {
        val stacks = initialStacks()
        val operations = operations()
        operations.forEach { op ->
            repeat(op.qty) {
                executeOperation(stacks, op)
            }
        }

        return getTopCrateWord(stacks)
    }

    fun part2(): String {
        val stacks = initialStacks()
        val operations = operations()
        operations.forEach { op ->
            if (op.qty == 1) {
                repeat(op.qty) {
                    executeOperation(stacks, op)
                }
            } else if (op.qty > 1) {
                executeOperation9001(stacks, op)
            }
        }

        return getTopCrateWord(stacks)
    }

    private fun executeOperation(stacks: List<Stack<String>>, operation: Operation) {
        val srcStack = stacks[operation.source - 1]
        val destStack = stacks[operation.dest - 1]
        val crate = srcStack.pop()
        destStack.push(crate)
    }

    private fun executeOperation9001(stacks: List<Stack<String>>, operation: Operation) {
        val srcStack = stacks[operation.source - 1]
        val destStack = stacks[operation.dest - 1]
        val intermediateStack = Stack<String>()
        repeat(operation.qty) {
            intermediateStack.push(srcStack.pop())
        }
        while (!intermediateStack.isEmpty()) {
            destStack.push(intermediateStack.pop())
        }
    }

    private fun getTopCrateWord(stacks: List<Stack<String>>): String {
        return stacks.joinToString(separator = "") { it.pop() }
    }
}