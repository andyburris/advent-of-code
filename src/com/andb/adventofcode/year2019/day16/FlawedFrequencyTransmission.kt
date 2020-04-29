package com.andb.adventofcode.year2019.day16

import java.io.File
import kotlin.math.absoluteValue

private val reader = File("src/com/andb/adventofcode/year2019/day16/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day16/test.txt").bufferedReader()

fun main() {
    //println(listOf(1, 2, 3).repeated(3))
    partTwo()
}

private fun partOne() {
    val input: List<Int> = reader.readLine().toString().map { it.toString().toInt() }
    val final = (1..100).toList().reduceGeneric(input) { _, nextInput -> parsePhase(nextInput) }
    println(final.take(8).joinToString(separator = ""))
}

private fun partTwo() {
    val input: List<Int> = reader.readLine().toString().map { it.toString().toInt() }
    println("inputSize = ${input.size}")
    val offset = input.take(7).joinToString(separator = "").toInt()
    println("offset = $offset")
    val repeated = input.repeated(10000)
    println("repeatedSize = ${repeated.size}")
    val final = (1..100).toList().reduceGeneric(repeated) { phase, nextInput ->
        println("parsed $phase")
        parsePhase(nextInput)
    }
    println("finalSize = ${final.size}")
    println(final.subList(offset, offset + 8).joinToString(separator = ""))
}

private fun parsePhase(input: List<Int>): List<Int> {
    val output = mutableListOf<Int>()
    for (i in input.indices) {
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