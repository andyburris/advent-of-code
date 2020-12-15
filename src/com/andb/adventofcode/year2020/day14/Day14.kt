package com.andb.adventofcode.year2020.day14

import com.andb.adventofcode.year2019.common.sumByLong
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2020/day14/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day14/test.txt").bufferedReader()

fun main(){
    //partOne()
    partTwo()
}

private fun partOne(){
    var mask = reader.readLine().removePrefix("mask = ")
    println(11UL.maskV1(mask))
    val instructions = reader.readLines().map { it.toInstruction() }
    println(instructions)
    val memory = mutableMapOf<ULong, ULong>()
    instructions.forEach {
        when (it) {
            is Instruction.SetMemory -> memory[it.location] = it.value.maskV1(mask)
            is Instruction.SetMask -> mask = it.mask
        }
    }
    println(memory.sumByLong { it.value.toLong() })
}

private fun partTwo(){
    var mask = reader.readLine().removePrefix("mask = ")
    val instructions = reader.readLines().map { it.toInstruction() }
    val memory = mutableMapOf<ULong, ULong>()
    instructions.forEach {
        when (it) {
            is Instruction.SetMemory -> {
                val memoryLocations = it.location.maskV2(mask)
                println("memoryLocations for $instructions = $memoryLocations")
                memoryLocations.forEach { location ->
                    memory[location] = it.value
                }
            }
            is Instruction.SetMask -> mask = it.mask
        }
    }
    println(memory.sumByLong { it.value.toLong() })
}

private fun test(){

}

private fun ULong.maskV1(bitmask: String): ULong {
    val string = this.toString(2)
    val padded = string.padStart(36, '0')
    println("padded for $this = $padded (${padded.length})")
    return bitmask.mapIndexed { index: Int, c: Char ->
        when (c) {
            'X' -> padded[index]
            '0', '1' -> c
            else -> throw Error("Must be one of X, 0, or 1 (currently '$c')")
        }
    }.joinToString("").toULong(2)
}

private fun ULong.maskV2(bitmask: String): List<ULong> {
    val string = this.toString(2)
    val padded = string.padStart(36, '0')
    val onesOverwritten = bitmask.mapIndexed { index: Int, c: Char ->
        when (c) {
            '1', 'X' -> c
            else -> padded[index]
        }
    }.joinToString("")
    println("onesOverwritten = $onesOverwritten")
    var enumerateFloating = listOf(onesOverwritten)
    while (enumerateFloating.any { it.contains('X') }) {
        enumerateFloating = enumerateFloating.map {
            listOf(it.replaceFirst('X', '0'), it.replaceFirst('X', '1'))
        }.flatten()
    }
    println("enumerateFloating = $enumerateFloating")
    return enumerateFloating.map { it.toULong(2) }
}

private sealed class Instruction {
    data class SetMemory(val location: ULong, val value: ULong) : Instruction()
    data class SetMask(val mask: String) : Instruction()
}
private fun String.toInstruction(): Instruction {
    return when {
        this.startsWith("mem") -> {
            val (address, value) = this.removePrefix("mem[").split("] = ")
            Instruction.SetMemory(address.toULong(), value.toULong())
        }
        this.startsWith("mask") -> Instruction.SetMask(this.removePrefix("mask = "))
        else -> throw Error("invalid line = $this")
    }
}