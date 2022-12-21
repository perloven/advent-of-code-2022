package se.perloven.aoc2022.day21

import se.perloven.aoc2022.util.ResourceFiles

fun main() {
    println("Part 1: ${MonkeyMatch.part1()}")
    println("Part 2: ${MonkeyMatch.part2()}")
}

object MonkeyMatch {

    private sealed interface Operation {
        fun execute(monkeys: Map<String, Monkey>): Long
    }

    private class Value(private val value: Int) : Operation {
        override fun execute(monkeys: Map<String, Monkey>): Long {
            return value.toLong()
        }
    }

    private class Add(private val left: String, private val right: String) : Operation {
        override fun execute(monkeys: Map<String, Monkey>): Long {
            val leftMonkey = monkeys[left] ?: throw IllegalStateException("Monkey $left not found")
            val rightMonkey = monkeys[right] ?: throw IllegalStateException("Monkey $right not found")
            return leftMonkey.operation.execute(monkeys) + rightMonkey.operation.execute(monkeys)
        }
    }

    private class Subtract(private val left: String, private val right: String) : Operation {
        override fun execute(monkeys: Map<String, Monkey>): Long {
            val leftMonkey = monkeys[left] ?: throw IllegalStateException("Monkey $left not found")
            val rightMonkey = monkeys[right] ?: throw IllegalStateException("Monkey $right not found")
            return leftMonkey.operation.execute(monkeys) - rightMonkey.operation.execute(monkeys)
        }
    }

    private class Multiply(private val left: String, private val right: String) : Operation {
        override fun execute(monkeys: Map<String, Monkey>): Long {
            val leftMonkey = monkeys[left] ?: throw IllegalStateException("Monkey $left not found")
            val rightMonkey = monkeys[right] ?: throw IllegalStateException("Monkey $right not found")
            return leftMonkey.operation.execute(monkeys) * rightMonkey.operation.execute(monkeys)
        }
    }

    private class Divide(private val left: String, private val right: String) : Operation {
        override fun execute(monkeys: Map<String, Monkey>): Long {
            val leftMonkey = monkeys[left] ?: throw IllegalStateException("Monkey $left not found")
            val rightMonkey = monkeys[right] ?: throw IllegalStateException("Monkey $right not found")
            return leftMonkey.operation.execute(monkeys) / rightMonkey.operation.execute(monkeys)
        }
    }

    private data class Monkey(val name: String, val operation: Operation)

    fun part1(): Long {
        val monkeys: Map<String, Monkey> = parseMonkeyInput()

        val rootMonkey = monkeys["root"] ?: throw IllegalStateException("root monkey not found")

        return rootMonkey.operation.execute(monkeys)
    }

    fun part2(): Long {
        return -2
    }

    private fun parseMonkeyInput(): Map<String, Monkey> {
        val monkeyList = ResourceFiles.readLinesSplit(21).map { parseMonkey(it) }
        return monkeyList.associateBy { it.name }
    }

    private fun parseMonkey(line: List<String>): Monkey {
        val name = line[0].dropLast(1)
        val testValue = line[1].toIntOrNull()
        val operation = if (testValue != null) {
            Value(testValue)
        } else if (line[2] == "+")  {
            Add(left = line[1], right = line[3])
        } else if (line[2] == "-") {
            Subtract(left = line[1], right = line[3])
        } else if (line[2] == "*") {
            Multiply(left = line[1], right = line[3])
        } else if (line[2] == "/") {
            Divide(left = line[1], right = line[3])
        } else {
            throw IllegalArgumentException("Unable to parse monkey from line: $line")
        }

        return Monkey(name = name, operation = operation)
    }
}