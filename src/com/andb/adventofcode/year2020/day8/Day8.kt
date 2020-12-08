package com.andb.adventofcode.year2020.day8

import com.andb.adventofcode.year2020.common.*
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2020/day8/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day8/test.txt").bufferedReader()

fun main(){
    //partOne()
    partTwo()
}

private fun partOne(){
    val instructions = reader.readLines().mapIndexed { index, line -> line.toInstruction(index) }
    println(instructions)
    println(instructions.execute())
}

private fun partTwo(){
    val instructions = reader.readLines().mapIndexed { index, line -> line.toInstruction(index) }
    val instructionToFlip = instructions.first { instruction ->
        val withInstructionFlipped = instructions.map { if (it == instruction) instruction.flipped() else it }
        val result = withInstructionFlipped.execute()
        return@first result is ConsoleResult.Success
    }
    val withFlipped = instructions.mapAt(instructionToFlip.index) { it.flipped() }
    println(withFlipped.execute())
}
