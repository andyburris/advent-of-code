package com.andb.adventofcode.year2021.day1

import java.io.File

private val reader = File("src/com/andb/adventofcode/year2021/day1/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2021/day1/test.txt").bufferedReader()

fun main() {
    partTwo()
}

private fun partOne() {
    val depths = reader.readLines().map { it.toInt() }
    val amountIncreasing = depths.windowed(2).count { it[0] < it[1] }
    println(amountIncreasing)
}

private fun partTwo() {
    val depths = reader.readLines().map { it.toInt() }
    val amountWindowsIncreasing = depths
        .windowed(3)
        .map { it.sum() }
        .windowed(2)
        .count { it[0] < it[1] }
    println(amountWindowsIncreasing)
}

private fun test() {

}