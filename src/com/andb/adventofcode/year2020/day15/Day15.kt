package com.andb.adventofcode.year2020.day15

import java.io.File

private val reader = File("src/com/andb/adventofcode/year2020/day15/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day15/test.txt").bufferedReader()

fun main(){
    val startingNumbers = reader.readLine().split(",").map { it.toInt() }
    partOne(startingNumbers)
    partTwo(startingNumbers)
}

private fun partOne(startingNumbers: List<Int>){
    println(startingNumbers.playGame(2020))
}

private fun partTwo(startingNumbers: List<Int>){
    println(startingNumbers.playGame(30000000))
}

private fun List<Int>.playGame(turns: Int): Int {
    val said = this.dropLast(1).mapIndexed { index: Int, i: Int -> i to index }.toMap().toMutableMap()
    var index = this.size
    var last = this.last()
    repeat(turns - index) {
        val next = if (said.containsKey(last)) index - said[last]!! - 1 else 0
        said[last] = index - 1
        index++
        last = next
    }
    return last
}