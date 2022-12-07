package se.perloven.aoc2022.day7

import se.perloven.aoc2022.util.ResourceFiles
import java.util.*

fun main() {
    println("Part 1: ${NoSpaceLeftOnDevice.part1()}")
    println("Part 2: ${NoSpaceLeftOnDevice.part2()}")
}

object NoSpaceLeftOnDevice {

    private data class File(val name: String, var size: Int, val subfiles: MutableList<File> = mutableListOf()) {
        override fun toString(): String {
            return "File($name, $size, subfiles:\n$subfiles)"
        }
        fun isDirectory(): Boolean {
            return subfiles.isNotEmpty()
        }

        fun totalSize(): Int {
            if (!isDirectory()) {
                return size
            }

            return subfiles.sumOf { it.totalSize() }
        }
    }

    private sealed interface Command

    private class CdCommand(val directory: String) : Command {

        fun isMoveOut(): Boolean {
            return directory == ".."
        }

        override fun toString(): String {
            return "[CD $directory]"
        }
    }

    private class LsCommand(val content: List<Content>) : Command {

        override fun toString(): String {
            return "[LS ${content.joinToString()}]"
        }
    }

    private data class Content(val type: String, val name: String) {
        override fun toString(): String {
            return "[Content $type $name]"
        }

        fun toFile(): File {
            return if (type.toIntOrNull() != null) {
                File(name = name, size = type.toInt())
            } else if (type == "dir") {
                File(name = name, size = 0)
            } else {
                throw IllegalStateException("Unhandled content $this")
            }
        }
    }


    fun part1(): Int {
        val commands = parseCommands()
        println(commands)
        val rootFile = parseFiles(commands)
        println(rootFile)
        return calculateTotalSize(rootFile, 0)
    }

    private fun parseCommands(): List<Command> {
        val lines = ResourceFiles.readLinesSplit("day7/input-1.txt")
        val commands = mutableListOf<Command>()
        for (i in lines.indices) {
            val line = lines[i]
            if (line[0] == "$") {
                val command = when(val commandName = line[1]) {
                    "cd" -> CdCommand(line[2])
                    "ls" -> LsCommand(parseLsContent(lines, i + 1))
                    else -> throw IllegalStateException("Unhandled command $commandName")
                }
                commands.add(command)
            }
        }
        return commands
    }

    private fun parseLsContent(lines: List<List<String>>, startIndex: Int): List<Content> {
        val content = mutableListOf<Content>()
        for (i in startIndex until lines.size) {
            val line = lines[i]
            if (line[0] == "$") {
                break
            }

            content.add(Content(
                type = line[0],
                name = line[1]
            ))
        }

        return content
    }

    private fun parseFiles(commands: List<Command>): File {
        val rootFile = File("/", 0, mutableListOf())
        val parentFiles: Stack<File> = Stack()
        var currentFile = rootFile
        for (command in commands.drop(1)) {
            if (command is CdCommand) {
                if (command.isMoveOut()) {
                    currentFile = parentFiles.pop()
                } else {
                    parentFiles.push(currentFile)
                    currentFile = currentFile.subfiles.firstOrNull { it.name == command.directory}
                        ?: File(command.directory, size = 0)
                }
            } else if (command is LsCommand) {
                currentFile.subfiles.addAll(command.content.map { it.toFile() })
            }
        }

        return rootFile
    }

    private fun calculateTotalSize(rootFile: File, cleanupNeeded: Int): Int {
        val files = mutableListOf(rootFile)
        addFiles(files, rootFile)
        return files
            .filter { it.isDirectory() }
            .filter { it.totalSize() > cleanupNeeded }
            .minByOrNull { it.totalSize() }!!.totalSize()
    }

    private fun addFiles(files: MutableList<File>, currentFile: File) {
        files.addAll(currentFile.subfiles)
        currentFile.subfiles.forEach { addFiles(files, it) }
    }

    /*
    private fun addDirectory(directories: MutableList<File>, currentFile: File): Int {
        val currentSize = currentFile.totalSize()
        if (currentFile.isDirectory() && currentSize > 100_000) {
            return currentFile.subfiles.sumOf { addToSize(runningTotal, it) }
        }

        return currentFile.subfiles.sumOf { addToSize(runningTotal + currentSize, it) }
    }

     */

    fun part2(): Int {
        val commands = parseCommands()
        println(commands)
        val rootFile = parseFiles(commands)
        println(rootFile)
        val totalSize = rootFile.subfiles.sumOf { it.totalSize() }
        val freespace = 70_000_000 - totalSize
        val cleanupNeeded = 30_000_000 - freespace
        return calculateTotalSize(rootFile, cleanupNeeded)
    }
}