package com.andb.adventofcode.year2022.day5

import com.andb.adventofcode.year2019.day8.splitEvery
import com.andb.adventofcode.year2020.common.splitToGroups
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2022/day5/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day5/test.txt").bufferedReader()

fun main(){
    val (startingCratesRaw, instructionsRaw) = reader.readLines()
        .splitToGroups { it == "" }
    val startingRows = startingCratesRaw.dropLast(1)
        .map { line -> line.toList().splitEvery(4).map { it.joinToString("").trim(' ', '[', ']') } }
    val numColumns = startingRows.maxOf { it.size }
    val startingColumns = (0 until numColumns).map { columnIndex ->
        startingRows.reversed().mapNotNull { it.getOrNull(columnIndex) }.filter { it.isNotBlank() }.map { it.first() }
    }

    val instructions = instructionsRaw.map { it.toInstruction() }
    partTwo(startingColumns, instructions)
}

private fun partOne(startingColumns: List<List<Char>>, instructions: List<Instruction>){
    val finalColumns = instructions.fold(startingColumns) { acc, instruction ->
        acc.applyInstruction(instruction)
    }
    println(finalColumns.map { it.last() }.joinToString(""))
}

private fun partTwo(startingColumns: List<List<Char>>, instructions: List<Instruction>){
    val finalColumns = instructions.fold(startingColumns) { acc, instruction ->
        acc.applyInstructionTwo(instruction)
    }

    println(finalColumns.map { it.last() }.joinToString(""))
}

private fun test(){

}

private data class Instruction(val moveAmount: Int, val fromIndex: Int, val toIndex: Int)
private fun String.toInstruction(): Instruction {
    val moveAmount = this.drop(5).takeWhile { it in '0'..'9' }.toInt()
    val fromIndex = this.drop(5).drop(moveAmount.toString().length + 6).takeWhile { it in '0'..'9' }.toInt()
    val toIndex = this.takeLastWhile { it in '0'..'9' }.toInt()
    return Instruction(moveAmount, fromIndex - 1, toIndex - 1)
}

private fun List<List<Char>>.applyInstruction(instruction: Instruction): List<List<Char>> {
    val cratesToMove = this[instruction.fromIndex].takeLast(instruction.moveAmount)
    return this.mapIndexed { index, column ->
        when (index) {
            instruction.fromIndex -> column.dropLast(instruction.moveAmount)
            instruction.toIndex -> column + cratesToMove.reversed()
            else -> column
        }
    }
}

private fun List<List<Char>>.applyInstructionTwo(instruction: Instruction): List<List<Char>> {
    val cratesToMove = this[instruction.fromIndex].takeLast(instruction.moveAmount)
    return this.mapIndexed { index, column ->
        when (index) {
            instruction.fromIndex -> column.dropLast(instruction.moveAmount)
            instruction.toIndex -> column + cratesToMove
            else -> column
        }
    }
}