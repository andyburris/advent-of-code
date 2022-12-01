package com.andb.adventofcode.year2021.day8

import java.io.File

private val reader = File("src/com/andb/adventofcode/year2021/day8/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2021/day8/test.txt").bufferedReader()

fun main(){
    partTwo()
}

private fun partOne(){
    val entriesOutputs = reader.readLines().map { line ->
        val (patternsText, outputText) = line.split(" | ")
        val outputDigits = outputText.split(" ")
        outputDigits
    }
    val amount1478 = entriesOutputs.flatten().count { it.length in listOf(2, 3, 4, 7) }
    println(amount1478)
}

private fun partTwo(){
    val summedOutputs = reader.readLines().sumBy { calculateOutput(it) }
    println(summedOutputs)
}

private fun calculateOutput(input: String): Int {
    val (patternsText, outputText) = input.split(" | ")
    val patterns = patternsText.split(" ")
    val outputDigitsSegments = outputText.split(" ")
    val remainingPatterns = patterns.toMutableList()
    val uniqueMappings = mapOf(
        1 to remainingPatterns.popFirst { it.length == 2 },
        4 to remainingPatterns.popFirst { it.length == 4 },
        7 to remainingPatterns.popFirst { it.length == 3 },
        8 to remainingPatterns.popFirst { it.length == 7 },
    )
    val mapping9 = 9 to remainingPatterns.popFirst { it.length == 6 && uniqueMappings.getValue(4).overlaps(it) }
    val mapping0 = 0 to remainingPatterns.popFirst { it.length == 6 && uniqueMappings.getValue(1).overlaps(it) }
    val mapping6 = 6 to remainingPatterns.popFirst { it.length == 6 }
    val sixDigitMappings = uniqueMappings + listOf(mapping9, mapping0, mapping6)

    val mapping5 = 5 to remainingPatterns.popFirst { it.overlaps(sixDigitMappings.getValue(6)) }
    val mapping3 = 3 to remainingPatterns.popFirst { it.overlaps(sixDigitMappings.getValue(9)) }
    val mapping2 = 2 to remainingPatterns.first()

    val allMappings = sixDigitMappings + listOf(mapping5, mapping3, mapping2)
    return outputDigitsSegments
        .map { segment -> allMappings.entries.first { it.value isSameDigitAs segment }.key }
        .digitize()
}

private fun <T> MutableList<T>.popFirst(predicate: (T) -> Boolean): T {
    val toRemove = this.first(predicate)
    this.remove(toRemove)
    return toRemove
}
private fun String.overlaps(other: String) = this.all { it in other }
private infix fun String.isSameDigitAs(other: String) = this.toSortedSet() == other.toSortedSet()
private fun List<Int>.digitize() = this.fold(0) { acc, digit -> (acc * 10) + digit }

private fun test(){

}