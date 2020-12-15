package com.andb.adventofcode.year2020.day14

import com.andb.adventofcode.year2019.common.sumByLong
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2020/day14/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day14/test.txt").bufferedReader()

fun main(){
    val instructions = reader.readLines().map { it.toInstruction() }
    partOne(instructions)
    partTwo(instructions)
}

private fun partOne(instructions: List<Instruction>){
    println(instructions.executeV1().memory.sumByLong { it.value.toLong() })
}

private fun partTwo(instructions: List<Instruction>){
    println(instructions.executeV2().memory.sumByLong { it.value.toLong() })
}

private fun List<Instruction>.executeV1() = this.fold(PortComputer()) { computer, instruction ->
    when (instruction) {
        is Instruction.SetMemory -> computer.copy(memory = computer.memory.withValue(instruction.location, instruction.value.maskV1(computer.mask)))
        is Instruction.SetMask -> computer.copy(mask = instruction.mask)
    }
}

private fun List<Instruction>.executeV2() = this.fold(PortComputer()) { computer, instruction ->
    when (instruction) {
        is Instruction.SetMemory -> {
            val memoryLocations = instruction.location.maskV2(computer.mask)
            computer.copy(memory = computer.memory.withValues(memoryLocations.map { it to instruction.value }))
        }
        is Instruction.SetMask -> computer.copy(mask = instruction.mask)
    }
}

private fun ULong.maskV1(bitmask: String): ULong {
    val padded = this.toString(2).padStart(36, '0')
    return bitmask.mapIndexed { index: Int, c: Char ->
        when (c) {
            'X' -> padded[index]
            '0', '1' -> c
            else -> throw Error("Must be one of X, 0, or 1 (currently '$c')")
        }
    }.joinToString("").toULong(2)
}

private fun ULong.maskV2(bitmask: String): List<ULong> {
    val padded = this.toString(2).padStart(36, '0')
    val onesOverwritten = bitmask
        .mapIndexed { index: Int, c: Char ->
            when (c) {
                '1', 'X' -> c
                else -> padded[index]
            }
        }.joinToString("")
    return onesOverwritten.fold(listOf(onesOverwritten)) { acc, c ->
        if (c != 'X') return@fold acc
        acc.map { listOf(it.replaceFirst('X', '0'), it.replaceFirst('X', '1')) }.flatten()
    }.map { it.toULong(2) }
}

private data class PortComputer(val mask: String = "", val memory: Map<ULong, ULong> = mutableMapOf<ULong, ULong>())

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

fun <K, V> Map<K, V>.withValue(key: K, value: V) = this.toMutableMap().also { it[key] = value }.toMap()
fun <K, V> Map<K, V>.withValues(values: List<Pair<K, V>>) = this.toMutableMap().also { it.putAll(values) }.toMap()
