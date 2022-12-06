package com.andb.adventofcode.year2022.day6

import java.io.File

private val reader = File("src/com/andb/adventofcode/year2022/day6/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day6/test.txt").bufferedReader()

fun main(){
    //partOne()
    partTwo()
}

private fun partOne(){
    println(findDistinctMarkerEndIndex(4))
}

private fun partTwo(){
    println(findDistinctMarkerEndIndex(14))
}

private fun findDistinctMarkerEndIndex(length: Int): Int {
    val input = reader.readLine()
    val marker = input.toList()
        .windowed(length)
        .first { it.distinct().size == length }
        .joinToString("")
    val markerIndex = input.indexOf(marker)
    return markerIndex + length
}