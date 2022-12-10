package com.andb.adventofcode.year2022.day10

import com.andb.adventofcode.year2019.day8.splitEvery
import com.andb.adventofcode.year2020.common.takeEvery
import java.io.File
import java.lang.Error

private val reader = File("src/com/andb/adventofcode/year2022/day10/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day10/test.txt").bufferedReader()
private val test2Reader = File("src/com/andb/adventofcode/year2022/day10/test2.txt").bufferedReader()

fun main(){
    val instructions: List<Instruction> = reader.readLines().map { it.toInstruction() }
    val spacedInstructions: List<Instruction?> = instructions.flatMap {
        when(it) {
            is Instruction.AddX -> listOf(null, it)
            Instruction.NoOp -> listOf(it)
        }
    }

    //partOne(spacedInstructions)
    partTwo(spacedInstructions)
}

private fun partOne(spacedInstructions: List<Instruction?>){


    val indices = listOf(20, 60, 100, 140, 180, 220)
    val every20 = indices.map { cycle ->
        cycle * (spacedInstructions.take(cycle - 1).filterIsInstance<Instruction.AddX>().sumOf { it.amount } + 1)
    }
    println(every20.sumOf { it })
}

private fun partTwo(spacedInstructions: List<Instruction?>){
    val drawnCharacters: List<Boolean> = spacedInstructions.foldIndexed(emptyList<Boolean>() to 1) { index, (acc, register), instruction ->
        val spriteWindow = (register - 1)..(register + 1)
        val isSpriteDrawn = (index % 40) in spriteWindow
        val newRegister = when(instruction) {
            is Instruction.AddX -> register + instruction.amount
            Instruction.NoOp, null -> register
        }
        (acc + isSpriteDrawn) to newRegister
    }.first

    val printable = drawnCharacters.printable()
    println(printable)
}

private fun test(){

}

sealed class Instruction {
    object NoOp : Instruction()
    data class AddX(val amount: Int) : Instruction()
}

private fun String.toInstruction() = when(this.take(4)) {
    "noop" -> Instruction.NoOp
    "addx" -> Instruction.AddX(this.drop(5).toInt())
    else -> throw Error("${this.take(4)} is invalid")
}

private fun List<Boolean>.printable() = this
    .map {
        when(it) {
            true -> '#'
            false -> '.'
        }
    }
    .splitEvery(40)
    .joinToString("\n") {
        it.joinToString("")
    }