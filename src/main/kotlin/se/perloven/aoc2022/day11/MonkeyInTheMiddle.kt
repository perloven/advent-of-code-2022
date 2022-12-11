package se.perloven.aoc2022.day11

import se.perloven.aoc2022.util.ResourceFiles

fun main() {
    println("Part 1: ${MonkeyInTheMiddle.part1()}")
    println("Part 2: ${MonkeyInTheMiddle.part2()}")
}

object MonkeyInTheMiddle {

    private data class Monkey(val id: Int, val operation: (Int) -> Int, val testDenominator: Int, val trueTarget: Int,
                              val falseTarget: Int, val items: MutableList<Item>, var inspections: Int = 0) {
        fun inspect(): List<ThrownItem> {
            val itemsToThrow = items.toList()
            inspections += items.size
            items.forEach { it.worryLevel = operation(it.worryLevel) / 3 }
            items.clear()
            return itemsToThrow.map { ThrownItem(target = decideTarget(it), item = it) }
        }

        private fun decideTarget(item: Item): Int {
            return if (item.worryLevel % testDenominator == 0) {
                trueTarget
            } else {
                falseTarget
            }
        }

        fun receiveThrownItem(item: Item) {
            items.add(item)
        }
    }

    private data class Item(var worryLevel: Int)

    private data class ThrownItem(val target: Int, val item: Item)

    fun part1(): Int {
        val monkeys = parseMonkeys()
        doMonkeyBusiness(monkeys)
        return calculateLevelOfMonkeyBusiness(monkeys)
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
                .map { Item(worryLevel = it.toInt()) }
                .toMutableList()
        )
    }

    private fun parseOperation(monkeyIndex: Int): (Int) -> Int {
        return when(monkeyIndex) {
            0 -> { old -> old * 3 }
            1 -> { old -> old * 17 }
            2 -> { old -> old + 1}
            3 -> { old -> old + 2 }
            4 -> { old -> old * old }
            5 -> { old -> old + 8 }
            6 -> { old -> old + 6 }
            7 -> { old -> old + 7 }
            else -> throw IllegalArgumentException("Invalid monkey index $monkeyIndex")
        }
    }

    private fun doMonkeyBusiness(monkeys: List<Monkey>) {
        repeat(20) {
            for (monkey in monkeys) {
                val itemsToThrow = monkey.inspect()
                itemsToThrow.forEach {
                    val targetMonkey = monkeys[it.target]
                    targetMonkey.receiveThrownItem(it.item)
                }
            }
        }
    }

    private fun calculateLevelOfMonkeyBusiness(monkeys: List<Monkey>): Int {
        return monkeys.map { it.inspections }
            .sortedByDescending { it }
            .take(2)
            .reduce { acc: Int, i: Int -> acc * i }
    }

    fun part2(): Int {
        return -1
    }
}