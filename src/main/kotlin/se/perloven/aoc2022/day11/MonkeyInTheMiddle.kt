package se.perloven.aoc2022.day11

import se.perloven.aoc2022.util.ResourceFiles
import java.math.BigInteger

fun main() {
    println("Part 1: ${MonkeyInTheMiddle.part1()}")
    println("Part 2: ${MonkeyInTheMiddle.part2()}")
}

object MonkeyInTheMiddle {

    private data class Monkey(val id: Int, val operation: (BigInteger) -> BigInteger, val testDenominator: Int, val trueTarget: Int,
                              val falseTarget: Int, val items: MutableList<Item>, var inspections: BigInteger = BigInteger.ZERO) {
        fun inspect(part: Int): List<ThrownItem> {
            val itemsToThrow = items.toList()
            inspections = inspections.add(BigInteger.valueOf(items.size.toLong()))
            items.forEach { it.worryLevel = reduceWorryLevel(part, operation(it.worryLevel)) }
            items.clear()
            return itemsToThrow.map { ThrownItem(target = decideTarget(it), item = it) }
        }

        private fun reduceWorryLevel(part: Int, worryLevel: BigInteger): BigInteger {
            return when(part) {
                1 -> worryLevel.div(BigInteger.valueOf(3))
                2 -> worryLevel.rem(BigInteger.valueOf(9699690))
                else -> throw IllegalArgumentException("Illegal part $part")
            }
        }

        private fun decideTarget(item: Item): Int {
            return if (item.worryLevel.rem(BigInteger.valueOf(testDenominator.toLong())) == BigInteger.ZERO) {
                trueTarget
            } else {
                falseTarget
            }
        }

        fun receiveThrownItem(item: Item) {
            items.add(item)
        }
    }

    private data class Item(var worryLevel: BigInteger)

    private data class ThrownItem(val target: Int, val item: Item)

    fun part1(): Int {
        val monkeys = parseMonkeys()
        doMonkeyBusiness(monkeys, 1)
        return calculateLevelOfMonkeyBusiness(monkeys).toInt()
    }

    private fun parseMonkeys(): List<Monkey> {
        return ResourceFiles.readLines(11)
            .filter(String::isNotBlank)
            .map { it.trim() }
            .chunked(6)
            .map { parseMonkey(it) }
    }

    private fun parseMonkey(lines: List<String>): Monkey {
        val sixLineList = lines.map { it.split(" ") }
        val id = sixLineList[0].last().take(1).toInt()
        return Monkey(
            id = id,
            operation = parseOperation(id),
            testDenominator = sixLineList[3].last().toInt(),
            trueTarget = sixLineList[4].last().toInt(),
            falseTarget = sixLineList[5].last().toInt(),
            items = sixLineList[1]
                .drop(2)
                .map { it.replace(",", "") }
                .map { Item(worryLevel = BigInteger.valueOf(it.toLong())) }
                .toMutableList()
        )
    }

    private fun parseOperation(monkeyIndex: Int): (BigInteger) -> BigInteger {
        return when(monkeyIndex) {
            0 -> { old -> old.multiply(BigInteger.valueOf(3)) }
            1 -> { old -> old.multiply(BigInteger.valueOf(17)) }
            2 -> { old -> old.add(BigInteger.valueOf(1)) }
            3 -> { old -> old.add(BigInteger.valueOf(2)) }
            4 -> { old -> old.pow(2) }
            5 -> { old -> old.add(BigInteger.valueOf(8)) }
            6 -> { old -> old.add(BigInteger.valueOf(6)) }
            7 -> { old -> old.add(BigInteger.valueOf(7)) }
            else -> throw IllegalArgumentException("Invalid monkey index $monkeyIndex")
        }
    }

    private fun doMonkeyBusiness(monkeys: List<Monkey>, part: Int) {
        val rounds = when(part) {
            1 -> 20
            2 -> 10_000
            else -> 0
        }
        repeat(rounds) { i ->
            for (monkey in monkeys) {
                val itemsToThrow = monkey.inspect(part)
                itemsToThrow.forEach { thrownItem ->
                    val targetMonkey = monkeys[thrownItem.target]
                    targetMonkey.receiveThrownItem(thrownItem.item)
                }
            }
        }
    }

    private fun calculateLevelOfMonkeyBusiness(monkeys: List<Monkey>): BigInteger {
        return monkeys.map { it.inspections }
            .sortedByDescending { it }
            .take(2)
            .reduce { acc: BigInteger, i: BigInteger -> acc.multiply(i) }
    }

    fun part2(): BigInteger {
        val monkeys = parseMonkeys()
        doMonkeyBusiness(monkeys, 2)
        return calculateLevelOfMonkeyBusiness(monkeys)
    }
}