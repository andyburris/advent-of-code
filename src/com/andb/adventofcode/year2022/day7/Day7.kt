package com.andb.adventofcode.year2022.day7

import com.andb.adventofcode.year2020.common.splitToGroups
import java.io.File
import java.lang.Error

private val reader = File("src/com/andb/adventofcode/year2022/day7/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day7/test.txt").bufferedReader()

fun main(){
    val commandsRaw: List<List<String>> = reader.readLines().splitToGroups(dropSeparator = false) { it.first() == '$' }.filter { it.isNotEmpty() }
    val commands = commandsRaw.map { it.toCommand() }

    val allSizedDirectories = commands.fold(emptyList<String>() to emptyList<SizedDirectory>()) { (currentPath, acc), command ->
        when(command) {
            is Command.Cd -> if (command.directoryName == "..") currentPath.dropLast(1) to acc else currentPath.plus(command.directoryName) to acc
            is Command.Ls -> currentPath to acc.plus(
                SizedDirectory(
                    currentPath,
                    command.results.filterIsInstance<DirectoryItem.File>().map { it.size },
                    command.results.filterIsInstance<DirectoryItem.Directory>().map { currentPath + it.name }
                )
            )
        }
    }.second

    val allSizes = allSizedDirectories.map { it.calculateSize(allSizedDirectories) }

    //partOne(allSizes)
    partTwo(allSizes)
}

private fun partOne(allSizes: List<Int>){
    println(allSizes.filter { it <= 100000 }.sum())
}

private fun partTwo(allSizes: List<Int>){
    val rootSize = allSizes.maxOf { it }
    val alreadyFreeSpace = 70000000 - rootSize
    val needToFree = 30000000 - alreadyFreeSpace

    println(allSizes.filter { it >= needToFree }.minOf { it })
}

private fun test(){

}

private fun List<String>.toCommand(): Command {
    val input = this.first()
    val command = input.drop(2).take(2)
    return when(command) {
        "cd" -> Command.Cd(input.drop(5))
        "ls" -> Command.Ls(this.drop(1).map { it.toDirectoryItem() })
        else -> throw Error("$command is not a valid command")
    }
}

private fun String.toDirectoryItem() = when(this.take(3)) {
    "dir" -> DirectoryItem.Directory(this.drop(4))
    else -> DirectoryItem.File(this.takeWhile { it in '0'..'9' }.toInt(), this.takeLastWhile { it != ' ' })
}

private sealed class Command {
    data class Cd(val directoryName: String) : Command()
    data class Ls(val results: List<DirectoryItem>) : Command()
}

private sealed class DirectoryItem {
    data class File(val size: Int, val name: String) : DirectoryItem()
    data class Directory(val name: String) : DirectoryItem()
}

private data class SizedDirectory(
    val path: List<String>,
    val fileSizes: List<Int>,
    val directoryReferences: List<List<String>>
) {
    fun calculateSize(allSizes: List<SizedDirectory>): Int = fileSizes.sum() + allSizes.filter { it.path in directoryReferences }.sumOf { it.calculateSize(allSizes) }
}