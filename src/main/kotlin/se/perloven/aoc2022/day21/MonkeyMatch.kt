package se.perloven.aoc2022.day21

import se.perloven.aoc2022.util.ResourceFiles

fun main() {
    println("Part 1: ${MonkeyMatch.part1()}")
    println("Part 2: ${MonkeyMatch.part2()}")
}

object MonkeyMatch {

    private sealed interface Expression {
        fun evaluate(): Long
    }

    private sealed class BinaryExpression(
        var left: Expression? = null,
        var right: Expression? = null
    ) : Expression

    private class Value(var value: Long) : Expression {
        override fun evaluate(): Long {
            return value
        }
    }

    private class Add : BinaryExpression() {
        override fun evaluate(): Long {
            return left!!.evaluate() + right!!.evaluate()
        }
    }

    private class Subtract : BinaryExpression() {
        override fun evaluate(): Long {
            return left!!.evaluate() - right!!.evaluate()
        }
    }

    private class Multiply : BinaryExpression() {
        override fun evaluate(): Long {
            return left!!.evaluate() * right!!.evaluate()
        }
    }

    private class Divide : BinaryExpression() {
        override fun evaluate(): Long {
            return left!!.evaluate() / right!!.evaluate()
        }
    }

    private class Equal : BinaryExpression() {
        override fun evaluate(): Long {
            val leftValue = left!!.evaluate()
            val rightValue = right!!.evaluate()
            return if (leftValue == rightValue) {
                leftValue
            } else {
                throw IllegalStateException("Equal check not passed: $leftValue != $rightValue")
            }
        }
    }

    private data class Monkey(val name: String, val operandNames: Pair<String, String> = Pair("place", "holder"), val expression: Expression)

    fun part1(): Long {
        val monkeys: Map<String, Monkey> = parseMonkeyInputPart1()

        val rootMonkey = monkeys["root"] ?: throw IllegalStateException("root monkey not found")

        return rootMonkey.expression.evaluate()
    }

    private fun parseMonkeyInputPart1(): Map<String, Monkey> {
        val monkeyList = ResourceFiles.readLinesSplit(21).map { parseMonkeyPart1(it) }
        val monkeyMap: Map<String, Monkey> = monkeyList.associateBy { it.name }
        monkeyMap.forEach { (_, monkey) ->
            val monkeyExpression = monkey.expression
            if (monkeyExpression is BinaryExpression) {
                monkeyExpression.left = monkeyMap[monkey.operandNames.first]!!.expression
                monkeyExpression.right = monkeyMap[monkey.operandNames.second]!!.expression
            }
        }
        return monkeyMap
    }

    private fun parseMonkeyPart1(line: List<String>): Monkey {
        val name = line[0].dropLast(1)
        val testValue = line[1].toLongOrNull()
        val operation = if (testValue != null) {
            Value(testValue)
        } else if (line[2] == "+") {
            Add()
        } else if (line[2] == "-") {
            Subtract()
        } else if (line[2] == "*") {
            Multiply()
        } else if (line[2] == "/") {
            Divide()
        } else {
            throw IllegalArgumentException("Unable to parse monkey from line: $line")
        }

        return if (operation is Value) {
            Monkey(name = name, expression = operation)
        } else {
            Monkey(name = name, operandNames = Pair(line[1], line[3]), expression = operation)
        }
    }

    // not within range (-1000000)..1000000
    fun part2(): Long {
        val monkeys: Map<String, Monkey> = parseMonkeyInput()

        val rootMonkey = monkeys["root"] ?: throw IllegalStateException("root monkey not found")

        return findHumnValue(monkeys)
    }

    private fun findHumnValue(monkeys: Map<String, Monkey>): Long {
        val rootMonkey = monkeys["root"] ?: throw IllegalStateException("root monkey not found")
        val human = monkeys["humn"] ?: throw IllegalStateException("human not found")

        for (i in (-100)..100) {
            (human.expression as Value).value = i.toLong()
            try {
                rootMonkey.expression.evaluate()
            } catch (e: IllegalStateException) {
                continue
            }
            return i.toLong()
        }

        throw IllegalStateException("Human value not found")
    }

    private fun parseMonkeyInput(): Map<String, Monkey> {
        val monkeyList = ResourceFiles.readLinesSplit(21).map { parseMonkey(it) }
        val monkeyMap: Map<String, Monkey> = monkeyList.associateBy { it.name }
        monkeyMap.forEach { (_, monkey) ->
            val monkeyExpression = monkey.expression
            if (monkeyExpression is BinaryExpression) {
                monkeyExpression.left = monkeyMap[monkey.operandNames.first]!!.expression
                monkeyExpression.right = monkeyMap[monkey.operandNames.second]!!.expression
            }
        }
        return monkeyMap
    }

    private fun parseMonkey(line: List<String>): Monkey {
        val name = line[0].dropLast(1)
        val testValue = line[1].toLongOrNull()
        val operation = if (testValue != null) {
            Value(testValue)
        } else if (name == "root") {
            Equal()
        } else if (line[2] == "+") {
            Add()
        } else if (line[2] == "-") {
            Subtract()
        } else if (line[2] == "*") {
            Multiply()
        } else if (line[2] == "/") {
            Divide()
        } else {
            throw IllegalArgumentException("Unable to parse monkey from line: $line")
        }

        return if (operation is Value) {
            Monkey(name = name, expression = operation)
        } else {
            Monkey(name = name, operandNames = Pair(line[1], line[3]), expression = operation)
        }
    }
}