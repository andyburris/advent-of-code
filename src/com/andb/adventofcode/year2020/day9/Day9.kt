package com.andb.adventofcode.year2020.day9

import java.io.File

private val reader = File("src/com/andb/adventofcode/year2020/day9/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day9/test.txt").bufferedReader()

fun main(){
    val code = reader.readLines().map { it.toLong() }
    println(partOne(code))
    partTwo(code)
}

private fun partOne(code: List<Long>): Long{
    val preambleLength = 25
    return code.drop(preambleLength).filterIndexed { index, i ->
        !i.hasPairIn(code.slice(index until index + preambleLength))
    }.first()
}

private fun partTwo(code: List<Long>){
    val invalidNumber: Long = partOne(code)
    val contiguousSum = code.findContiguousSumTo(invalidNumber)
    println(contiguousSum)
    println(contiguousSum.min()!! + contiguousSum.max()!!)
}

private fun Long.hasPairIn(numbers: List<Long>): Boolean {
    return numbers.any { n1 -> numbers.any { n2 -> n1 + n2 == this } }
}

private fun List<Long>.findContiguousSumTo(sum: Long): List<Long> {
    this.forEachIndexed { index, l ->
        if (l >= sum) return@forEachIndexed
        val contiguous = emptyList<Long>().toMutableList()
        var sumIndex = 1
        while (contiguous.sum() < sum) {
            contiguous += this[index + sumIndex]
            if (contiguous.sum() == sum) {
                return contiguous
            } else {
                sumIndex++
            }
        }
    }
    return emptyList()
}