package se.perloven.aoc2022.day13

import se.perloven.aoc2022.util.ResourceFiles

fun main() {
    println("Part 1: ${DistressSignal.part1()}")
    println("Part 2: ${DistressSignal.part2()}")
}

object DistressSignal {

    private sealed interface Expression : Comparable<Expression>

    private data class Value(val value: Int) : Expression {
        override fun compareTo(other: Expression): Int {
            return when (other) {
                is Value -> this.value.compareTo(other.value)
                is ValueList -> compareToList(other)
            }
        }

        private fun compareToList(list: ValueList): Int {
            return if (list.list.isEmpty()) {
                1
            } else {
                compareTo(list.list.first())
            }
        }

        override fun toString(): String {
            return value.toString()
        }
    }

    private data class ValueList(val list: List<Expression>) : Expression {
        override fun compareTo(other: Expression): Int {
            return when (other) {
                is Value -> other.compareTo(this) * -1
                is ValueList -> compareLists(this, other)
            }
        }

        private fun compareLists(first: ValueList, second: ValueList): Int {
            if (first.list.isEmpty() && second.list.isEmpty()) {
                return 0
            } else if (first.list.isEmpty()) {
                return -1
            } else if (second.list.isEmpty()) {
                return 1
            }

            val firstValue = first.list.first()
            val secondValue = second.list.first()
            val compare = firstValue.compareTo(secondValue)
            if (compare == 0) {
                return compareLists(ValueList(first.list.drop(1)), ValueList(second.list.drop(1)))
            }

            //println("$compare : $first < $second")
            return compare
        }

        override fun toString(): String {
            return list.joinToString(separator = ",", prefix = "[", postfix = "]")
        }
    }

    // 6097 is not the right answer
    fun part1(): Int {
        val pairs: List<Pair<Expression, Expression>> = loadExpressions()
        printSerializedExpressions(pairs)
        val comparisons = pairs.map { it.first.compareTo(it.second) }
        return countSumOfOrderedPairs(comparisons)
    }

    private fun loadExpressions(): List<Pair<Expression, Expression>> {
        return ResourceFiles.readLines(13)
            .chunked(3)
            .map { parseExpressions(it[0], it[1]) }
    }

    private fun parseExpressions(firstLine: String, secondLine: String): Pair<Expression, Expression> {
        return Pair(
            first = parseExpression(firstLine),
            second = parseExpression(secondLine)
        )
    }

    private sealed interface Token
    private object ListOpen : Token {
        override fun toString(): String {
            return "["
        }
    }

    private object ListClose : Token {
        override fun toString(): String {
            return "]"
        }
    }

    private data class Number(val value: Int) : Token {
        override fun toString(): String {
            return value.toString()
        }
    }

    private fun parseExpression(line: String): Expression {
        val tokens = parseTokens(line.toList())
        printTokens(tokens)
        return ValueList(list = parseListValues(tokens.drop(1).dropLast(1)))
    }

    private fun printTokens(tokens: List<Token>) {
        println(tokens.joinToString(separator = " "))
    }

    // tokens: [ ] , number
    private fun parseTokens(chars: List<Char>): List<Token> {
        //println("Parsing $chars")
        val numbers = '0'.code..'9'.code
        val tokens = mutableListOf<Token>()
        var curNumber: String? = null
        for (char in chars) {
            if (char.code !in numbers && curNumber != null) {
                tokens.add(Number(curNumber.toInt()))
                curNumber = null
            }
            if (char.code in numbers) {
                curNumber = curNumber?.let { it + char.toString() } ?: char.toString()
            } else if (char == '[') {
                tokens.add(ListOpen)
            } else if (char == ']') {
                tokens.add(ListClose)
            } else if (char == ',') {
                // Do nothing, separators are not used in further parsing
            }
        }

        return tokens
    }

    private fun parseListValues(tokens: List<Token>): List<Expression> {
        //println("Parsing tokens $tokens")
        val listValues = mutableListOf<Expression>()
        for ((i, token) in tokens.withIndex()) {
            if (token is Number) {
                listValues.add(Value(token.value))
            } else if (token is ListOpen) {
                val nestedExpressions = parseListValues(closeList(tokens.drop(i + 1)))
                listValues.add(ValueList(list = nestedExpressions))
            } else if (token is ListClose) {
                continue
            }
        }

        return listValues
    }

    private fun closeList(tokens: List<Token>): List<Token> {
        var level = 1
        for ((i, token) in tokens.withIndex()) {
            if (token is ListOpen) {
                level++
            } else if (token is ListClose) {
                level--
            }
            if (level <= 0) {
                return tokens.take(i)
            }
        }

        throw IllegalStateException("Unable to close list with tokens: $tokens")
    }

    private fun countSumOfOrderedPairs(comparisonResults: List<Int>): Int {
        val indexes = mutableSetOf<Int>()
        var sum = 0
        for ((i, result) in comparisonResults.withIndex()) {
            if (result <= 0) {
                val index = i + 1
                indexes.add(index)
                sum += index
            }
        }
        println("Total comparisons: ${comparisonResults.size}")
        println("Ordered indexes: $indexes")

        return sum
    }

    private fun printSerializedExpressions(pairs: List<Pair<Expression, Expression>>) {
        pairs.forEach {
            println(it.first)
            println(it.second)
            println()
        }
    }

    fun part2(): Int {
        return -1
    }
}