package com.andb.adventofcode.year2022.day6

import java.io.File

private val reader = File("src/com/andb/adventofcode/year2022/day6/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day6/test.txt").bufferedReader()

fun main(){
    //partOne()
    partTwo()
}

private fun partOne(){
    val input = reader.readLine()
    val marker = input.toList()
        .windowed(4)
        .first { it.distinct().size == 4 }
        .joinToString("")
    val markerIndex = input.indexOf(marker)
    println(markerIndex + 4)
}

private fun partTwo(){
    val input = reader.readLine()
    val marker = input.toList()
        .windowed(14)
        .first { it.distinct().size == 14 }
        .joinToString("")
    val markerIndex = input.indexOf(marker)
    println(markerIndex + 14)
}

private fun test(){

}

private fun List<Char>.allDifferent(): Boolean {
    val seen = mutableListOf<Char>()
    return this.none {
        val alreadySeen = seen.contains(it)
        seen.add(it)
        alreadySeen
    }
}