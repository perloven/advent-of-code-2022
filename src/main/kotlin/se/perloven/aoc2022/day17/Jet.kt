package se.perloven.aoc2022.day17

import se.perloven.aoc2022.util.ResourceFiles

enum class Jet {
    PUSH_LEFT,
    PUSH_RIGHT;

    override fun toString(): String {
        return when (this) {
            PUSH_LEFT -> "<"
            PUSH_RIGHT -> ">"
        }
    }
}

class JetPattern(private val jets: List<Jet>) {
    private var index: Int = 0

    fun getNext(): Jet {
        if (index >= jets.size) {
            index = 0
        }
        val nextJet = jets[index]
        index++
        return nextJet
    }

    override fun toString(): String {
        return jets.joinToString(separator = " ")
    }

    fun size(): Int {
        return jets.size
    }
}

fun parseJetPattern(): JetPattern {
    return JetPattern(jets = ResourceFiles.readLines(17)[0].map { parseJet(it) })
}

private fun parseJet(char: Char): Jet {
    return when (char) {
        '<' -> Jet.PUSH_LEFT
        '>' -> Jet.PUSH_RIGHT
        else -> throw IllegalArgumentException("Invalid jet $char")
    }
}