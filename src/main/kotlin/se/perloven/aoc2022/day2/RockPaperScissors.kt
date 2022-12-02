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
        val lines = ResourceFiles.readLines("day2/input-1.txt")
        return lines.sumOf { countSingleScore(it) }
    }

    fun countRPSScore2(): Int {
        val lines = ResourceFiles.readLines("day2/input-1.txt")
        return lines.sumOf { countSingleScoreDynamic(it) }
    }

    private fun countSingleScore(line: String): Int {
        val actions = line.split(" ")
        val oppAction = decodeOppAction(actions[0])
        val yourAction = decodeYourAction(actions[1])
        return yourAction.score + getOutcomeScore(yourAction = yourAction, oppAction = oppAction)
    }

    private fun countSingleScoreDynamic(line: String): Int {
        val actions = line.split(" ")
        val oppAction = decodeOppAction(actions[0])
        return countOutcome(oppAction, actions[1])
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
        if (yourAction == oppAction) {
            return 3
        }

        return when (yourAction) {
            ROCK -> return when (oppAction) {
                PAPER -> 0
                SCISSORS -> 6
                else -> 0
            }

            PAPER -> return when (oppAction) {
                ROCK -> 6
                SCISSORS -> 0
                else -> 0
            }

            SCISSORS -> return when (oppAction) {
                ROCK -> 0
                PAPER -> 6
                else -> 0
            }
        }
    }
}