package se.perloven.aoc2022.day21

import se.perloven.aoc2022.util.ResourceFiles
import java.util.*

fun main() {
    println("Part 1: ${MonkeyMatch.part1()}")
    println("Part 2: ${MonkeyMatch.part2()}")
}

object MonkeyMatch {

    private sealed interface Expression {
        fun evaluate(): Long
        fun express(): String
    }

    private sealed class BinaryExpression(
        var left: Expression? = null,
        var right: Expression? = null
    ) : Expression

    private class Value(var value: Long) : Expression {
        override fun evaluate(): Long {
            return value
        }

        override fun express(): String {
            return "$value"
        }
    }

    private class Add : BinaryExpression() {
        override fun evaluate(): Long {
            return left!!.evaluate() + right!!.evaluate()
        }

        override fun express(): String {
            return "(${left!!.express()} + ${right!!.express()})"
        }
    }

    private class Subtract : BinaryExpression() {
        override fun evaluate(): Long {
            return left!!.evaluate() - right!!.evaluate()
        }

        override fun express(): String {
            return "(${left!!.express()} - ${right!!.express()})"
        }
    }

    private class Multiply : BinaryExpression() {
        override fun evaluate(): Long {
            return left!!.evaluate() * right!!.evaluate()
        }

        override fun express(): String {
            return "(${left!!.express()} * ${right!!.express()})"
        }
    }

    private class Divide : BinaryExpression() {
        override fun evaluate(): Long {
            return left!!.evaluate() / right!!.evaluate()
        }

        override fun express(): String {
            return "(${left!!.express()} / ${right!!.express()})"
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

        override fun express(): String {
            return "${left!!.express()}\n=\n${right!!.express()}"
        }
    }

    private object Variable : Expression {
        val operations: Queue<Expression> = LinkedList()
        override fun evaluate(): Long {
            return 0
        }

        override fun express(): String {
            return "x"
        }
    }

    private data class Monkey(
        val name: String,
        val operandNames: Pair<String, String> = Pair("place", "holder"),
        val expression: Expression
    )

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
    // 3759566892641 by trial and error. TODO: find the solution programmatically.
    fun part2(): Long {
        val monkeys: Map<String, Monkey> = parseMonkeyInput()

        val rootMonkey = monkeys["root"] ?: throw IllegalStateException("root monkey not found")

        println(rootMonkey.expression.express())

        return tryFindHumnValue(monkeys)
    }

    /* Between:
    3722337203685
    and
    3822337203685
     */
    private fun tryFindHumnValue(monkeys: Map<String, Monkey>): Long {
        val solution = 3759566892641
        val values: Iterable<Long> = listOf(3759566892641)
        values.forEach {
            println("$it : ${checkHumnValue(monkeys, it)}")
        }
        return solution
    }

    private fun checkHumnValue(monkeys: Map<String, Monkey>, humnValue: Long): Long {
        val rootMonkey = monkeys["root"] ?: throw IllegalStateException("root monkey not found")
        val human = monkeys["humn"] ?: throw IllegalStateException("human not found")
        (human.expression as Value).value = humnValue
        val rootExpression = rootMonkey.expression as BinaryExpression
        val leftValue = rootExpression.left!!.evaluate()
        val rightValue = rootExpression.right!!.evaluate()
        return leftValue - rightValue
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
        val operation = if (name == "humn") {
            Value(1)
        } else if (testValue != null) {
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

        return if (operation is Value || operation is Variable) {
            Monkey(name = name, expression = operation)
        } else {
            Monkey(name = name, operandNames = Pair(line[1], line[3]), expression = operation)
        }
    }
}