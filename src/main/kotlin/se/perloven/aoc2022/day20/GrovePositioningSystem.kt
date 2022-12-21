package se.perloven.aoc2022.day20

import se.perloven.aoc2022.util.ResourceFiles
import java.util.*

fun main() {
    println("Part 1: ${GrovePositioningSystem.part1()}")
    println("Part 2: ${GrovePositioningSystem.part2()}")
}

object GrovePositioningSystem {

    private data class EncryptedFile(private val numbers: MutableList<Number>) {
        private val size = numbers.size

        fun move (id: String) {
            //println(getNumbers())
            val number = numbers.first { it.id == id }
            val curPosition = numbers.indexOfFirst { it.id == id }
            val targetPosition = ((curPosition + number.value) + (20 * (size - 1))) % (size - 1)
            val removedNumber = if (targetPosition > curPosition) {
                numbers.add(targetPosition + 1, number)
                numbers.removeAt(curPosition)
            } else {
                val removed = numbers.removeAt(curPosition)
                numbers.add(targetPosition, number)
                removed
            }
            check(number.id == removedNumber.id) { "${number.id} != ${removedNumber.id}"}
        }

        fun findGroveCoordinates(): List<Number> {
            val zeroIndex = numbers.indexOfFirst { it.value == 0 }
            val coord1 = (zeroIndex + 1000) % size
            val coord2 = (zeroIndex + 2000) % size
            val coord3 = (zeroIndex + 3000) % size
            return listOf(numbers[coord1], numbers[coord2], numbers[coord3])
        }

        fun getNumbers(): List<Number> {
            return numbers
        }
    }

    private data class Number(val value: Int) {
        val id = UUID.randomUUID().toString()

        override fun equals(other: Any?): Boolean {
            return id == other
        }

        override fun hashCode(): Int {
            return id.hashCode()
        }

        override fun toString(): String {
            return value.toString()
        }
    }

    // 6328 is too low
    // 3540 is too low
    // -19092 is wrong
    fun part1(): Int {
        val numbers = ResourceFiles.readLines(20).map { parseNumber(it) }
        val mixingOrder: Queue<Number> = LinkedList(numbers)

        //println(numbers)
        return findSumOfCoordinates(numbers, mixingOrder)
    }

    private fun parseNumber(line: String): Number {
        return Number(line.toInt())
    }

    private fun findSumOfCoordinates(numbers: List<Number>, mixingOrder: Queue<Number>): Int {
        val encryptedFile = EncryptedFile(numbers.toMutableList())
        while (mixingOrder.peek() != null) {
            val next = mixingOrder.poll()
            encryptedFile.move(next.id)
        }

        val sumOfCoordinates = encryptedFile.findGroveCoordinates().sumOf { it.value }
        //println(encryptedFile.getNumbers())
        return sumOfCoordinates
    }

    fun part2(): Int {
        return -2
    }
}