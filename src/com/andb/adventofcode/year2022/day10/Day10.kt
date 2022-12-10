package com.andb.adventofcode.year2022.day10

import com.andb.adventofcode.year2019.day8.splitEvery
import com.andb.adventofcode.year2020.common.takeEvery
import java.io.File
import java.lang.Error

private val reader = File("src/com/andb/adventofcode/year2022/day10/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day10/test.txt").bufferedReader()
private val test2Reader = File("src/com/andb/adventofcode/year2022/day10/test2.txt").bufferedReader()

fun main(){
    //partOne()
    partTwo()
}

private fun partOne(){
    val instructions: List<Instruction> = reader.readLines().map { it.toInstruction() }
    val cycleLength: Int = instructions.sumOf {
        when(it) {
            is Instruction.AddX -> 2.toInt()
            Instruction.NoOp -> 1
        }
    }
    val spacedInstructions: List<Instruction?> = instructions.flatMap {
        when(it) {
            is Instruction.AddX -> listOf(null, it)
            Instruction.NoOp -> listOf(it)
        }
    }
    assert(spacedInstructions.size == cycleLength)

    val finalInfo = spacedInstructions.foldIndexed(CycleInfo()) { index, acc, instruction ->
        val newRegister = when(instruction) {
            is Instruction.AddX -> acc.register + instruction.amount
            Instruction.NoOp, null -> acc.register
        }
        CycleInfo(newRegister, acc.cycleSignalStrengthHistory + SignalStrength(index + 1, newRegister))
    }
    assert(finalInfo.register == 1 + instructions.filterIsInstance<Instruction.AddX>().sumOf { it.amount })

    println(finalInfo)
    val indices = listOf(20, 60, 100, 140, 180, 220)
//    val every20 = finalInfo.cycleSignalStrengthHistory.filterIndexed { index, strength -> strength.cycle in indices }
    val every20 = indices.map { cycle ->
        cycle * (spacedInstructions.take(cycle - 1).filterIsInstance<Instruction.AddX>().sumOf { it.amount } + 1)
    }
    println(every20)
//    println(every20.sumOf { it.strength })
    println(every20.sumOf { it })
}

private data class CycleInfo(val register: Int = 1, val cycleSignalStrengthHistory: List<SignalStrength> = emptyList())
private data class SignalStrength(val cycle: Int, val register: Int) {
    val strength = cycle * register
}

private fun partTwo(){
    val instructions: List<Instruction> = reader.readLines().map { it.toInstruction() }
    val spacedInstructions: List<Instruction?> = instructions.flatMap {
        when(it) {
            is Instruction.AddX -> listOf(null, it)
            Instruction.NoOp -> listOf(it)
        }
    }
    val drawnCharacters: List<Boolean> = spacedInstructions.foldIndexed(emptyList<Boolean>() to 1) { index, (acc, register), instruction ->
        //val cycle = index + 1

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
//    "addx" -> Instruction.AddX(this.takeLastWhile { it in '0'..'9' || it == '-' }.toInt())
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