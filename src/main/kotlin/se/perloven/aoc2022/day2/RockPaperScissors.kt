package se.perloven.aoc2022.day2

import se.perloven.aoc2022.day2.RockPaperScissors.Action.*
import se.perloven.aoc2022.util.ResourceFiles

fun main() {
    val solution = RockPaperScissors().countRPSScore()
    println("Part One: $solution")

    val solution2 = RockPaperScissors().countRPSScore2()
    println("Part Two: $solution2")
}

class RockPaperScissors {

    private enum class Action(val score: Int) {
        ROCK(1),
        PAPER(2),
        SCISSORS(3);

        fun getBeats(): Action {
            return when (this) {
                ROCK -> SCISSORS
                PAPER -> ROCK
                SCISSORS -> PAPER
            }
        }

        fun getBeatenBy(): Action {
            return when (this) {
                ROCK -> PAPER
                PAPER -> SCISSORS
                SCISSORS -> ROCK
            }
        }
    }

    fun countRPSScore(): Int {
        val lines = ResourceFiles.readLinesSplit(2)
        return lines.sumOf { countSingleScore(it) }
    }

    fun countRPSScore2(): Int {
        val lines = ResourceFiles.readLinesSplit(2)
        return lines.sumOf { countSingleScoreDynamic(it) }
    }

    private fun countSingleScore(lineParts: List<String>): Int {
        val oppAction = decodeOppAction(lineParts[0])
        val yourAction = decodeYourAction(lineParts[1])
        return yourAction.score + getOutcomeScore(yourAction = yourAction, oppAction = oppAction)
    }

    private fun countSingleScoreDynamic(lineParts: List<String>): Int {
        val oppAction = decodeOppAction(lineParts[0])
        return countOutcome(oppAction, lineParts[1])
    }

    private fun countOutcome(oppAction: Action, outcome: String): Int {
        val yourAction: Action = when (outcome) {
            "X" -> oppAction.getBeats()
            "Y" -> oppAction
            "Z" -> oppAction.getBeatenBy()
            else -> error("No such outcome $outcome")
        }
        return yourAction.score + getOutcomeScore(yourAction, oppAction)
    }

    private fun decodeOppAction(oppAction: String): Action {
        return when (oppAction) {
            "A" -> ROCK
            "B" -> PAPER
            "C" -> SCISSORS
            else -> throw IllegalArgumentException()
        }
    }

    private fun decodeYourAction(yourAction: String): Action {
        return when (yourAction) {
            "X" -> ROCK
            "Y" -> PAPER
            "Z" -> SCISSORS
            else -> throw IllegalArgumentException()
        }
    }

    private fun getOutcomeScore(yourAction: Action, oppAction: Action): Int {
        return if (yourAction == oppAction) {
            3
        } else if (yourAction.getBeats() == oppAction) {
            6
        } else {
            0
        }
    }
}