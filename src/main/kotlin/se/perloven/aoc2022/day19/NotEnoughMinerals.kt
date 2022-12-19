package se.perloven.aoc2022.day19

import se.perloven.aoc2022.util.ResourceFiles

fun main() {
    println("Part 1: ${NotEnoughMinerals.part1()}")
    println("Part 2: ${NotEnoughMinerals.part2()}")
}

object NotEnoughMinerals {

    private data class RobotCost(val ore: Int, val clay: Int = 0, val obsidian: Int = 0)

    private data class Blueprint(val id: Int, val oreRobotCost: RobotCost, val clayRobotCost: RobotCost,
                                 val obsidianRobotCost: RobotCost, val geodeRobotCost: RobotCost)

    fun part1(): Int {
        val blueprints = parseBlueprints()

        println(blueprints.joinToString(prefix = "Blueprints:\n=====\n", separator = "\n", postfix = "\n====="))
        return blueprints.sumOf { qualityLevel(it) }
    }

    private fun parseBlueprints(): List<Blueprint> {
        return ResourceFiles.readLines(19).map { parseBlueprint(it) }
    }

    private fun parseBlueprint(line: String): Blueprint {
        val splitLine = line.split(" ")
        return Blueprint(
            id = splitLine[1].dropLast(1).toInt(),
            oreRobotCost = RobotCost(ore = splitLine[6].toInt()),
            clayRobotCost = RobotCost(ore = splitLine[12].toInt()),
            obsidianRobotCost = RobotCost(ore = splitLine[18].toInt(), clay = splitLine[21].toInt()),
            geodeRobotCost = RobotCost(ore = splitLine[27].toInt(), obsidian = splitLine[30].toInt())
        )
    }

    private fun qualityLevel(blueprint: Blueprint): Int {
        return blueprint.id * maxGeodes(blueprint)
    }

    private fun maxGeodes(blueprint: Blueprint): Int {
        return 0
    }

    fun part2(): Int {
        return -2
    }
}