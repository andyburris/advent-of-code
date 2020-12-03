package com.andb.adventofcode.year2019.day16

import java.io.File
import kotlin.math.absoluteValue
import kotlin.system.measureTimeMillis

private val reader = File("src/com/andb/adventofcode/year2019/day16/input.txt").bufferedReader()
private val reader2 = File("src/com/andb/adventofcode/year2019/day16/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day16/test.txt").bufferedReader()

fun main() {
    //partOne()
    partTwo()
}

private fun partOne() {
    val input: List<Int> = reader.readLine().toString().map { it.toString().toInt() }
    val final = (1..100).toList().reduceGeneric(input) { _, nextInput -> parsePhase(nextInput) }
    println(final.take(8).joinToString(separator = ""))
}

private fun partTwo() {
    val input: List<Int> = reader.readLine().toString().map { it.toString().toInt() }
    val offset = input.take(7).joinToString(separator = "").toInt()
    println("offset = $offset")
    val repeated = input.repeated(10000)
    val currentInput = repeated.subList(offset, repeated.size).toMutableList()
    println("time = " + measureTimeMillis {
        repeat(100) { phase ->
            println("running phase $phase, input = ${currentInput.take(8)}")
            val sum = currentInput.sum()
            currentInput.toList().foldIndexed(sum) { index, acc, value ->
                if (index >= currentInput.size - 1) return@foldIndexed 0
                val newSum = acc - value
                currentInput[index + 1] = newSum.absoluteValue % 10
                return@foldIndexed newSum
            }
            currentInput[0] = sum.absoluteValue % 10
        }
    })
    println(currentInput.take(8).joinToString(separator = ""))
}


private fun test() {
    val input: MutableList<Int> = reader2.readLine().toString().map { it.toString().toInt() }.toMutableList()
    input[0] = 4
    input[2] = 4
    val dropped = input.dropLast(input.size - 8).toList()
    println("input = $input")
    println("dropped = $dropped")
    val final = (1..100).toList().reduceGeneric(input.toList()) { _, nextInput -> parsePhase(nextInput, input.size) }
    println(final.take(8).joinToString(separator = ""))
}

private fun parsePhase(input: List<Int>, iterations: Int = input.size): List<Int> {
    val output = mutableListOf<Int>()
    for (i in 0 until iterations) {
        output.add(parseInput(input, i))
    }
    return output
}

private fun parseInput(input: List<Int>, position: Int): Int {
    val pattern = listOf(0, 1, 0, -1)
    return input.mapIndexed { index: Int, i: Int ->
        val timesToRepeatPattern = position + 1
        val initialIndex = (index + 1) / timesToRepeatPattern
        val patternIndex = Math.floorMod(initialIndex, 4)
        i * pattern[patternIndex]
    }.sum().absoluteValue % 10
}

private fun <T, R> Iterable<T>.reduceGeneric(initial: R, operation: (T, acc: R) -> R): R {
    val iterator = this.iterator()
    if (!iterator.hasNext()) throw UnsupportedOperationException("Empty collection can't be reduced.")
    var accumlation = initial
    while (iterator.hasNext()) {
        accumlation = operation.invoke(iterator.next(), accumlation)
    }
    return accumlation
}

private fun <T> List<T>.repeated(amount: Int): List<T> {
    val output = mutableListOf<T>()
    for (i in 0 until amount) {
        output.addAll(this)
    }
    return output
}