package se.perloven.aoc2022.day11

import se.perloven.aoc2022.util.ResourceFiles
import java.math.BigInteger
import java.time.Duration
import java.time.Instant

fun main() {
    println("Part 1: ${MonkeyInTheMiddle.part1()}")
    println("Part 2: ${MonkeyInTheMiddle.part2()}")
}

object MonkeyInTheMiddle {

    private data class Monkey(val id: Int, val operation: (BigInteger) -> BigInteger, val testDenominator: Int, val trueTarget: Int,
                              val falseTarget: Int, val items: MutableList<Item>, var inspections: BigInteger = BigInteger.ZERO) {
        fun inspect(): List<ThrownItem> {
            val itemsToThrow = items.toList()
            inspections = inspections.add(BigInteger.valueOf(items.size.toLong()))
            items.forEach { it.worryLevel = operation(it.worryLevel) }
            items.clear()
            return itemsToThrow.map { ThrownItem(target = decideTarget(it), item = it) }
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

    fun part1(): String {
        val monkeys = parseMonkeys()
        doMonkeyBusiness(monkeys)
        return calculateLevelOfMonkeyBusiness(monkeys).toString()
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
            4 -> { old ->
                val start = Instant.now()
                val result = old.pow(2)
                val powDuration = Duration.between(start, Instant.now())
                println("pow duration: ${powDuration.toMillis()}, result: $result")
                result
            }
            5 -> { old -> old.add(BigInteger.valueOf(8)) }
            6 -> { old -> old.add(BigInteger.valueOf(6)) }
            7 -> { old -> old.add(BigInteger.valueOf(7)) }
            else -> throw IllegalArgumentException("Invalid monkey index $monkeyIndex")
        }
    }

    private fun doMonkeyBusiness(monkeys: List<Monkey>) {
        repeat(10_000) { i ->
            println(i)
            for (monkey in monkeys) {
                println("monkey ${monkey.id}")
                val itemsToThrow = monkey.inspect()
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

    fun part2(): Int {
        return -1
    }
}