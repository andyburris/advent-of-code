package com.andb.adventofcode.year2022.day4

import java.io.File
import java.lang.Error

private val reader = File("src/com/andb/adventofcode/year2022/day4/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day4/test.txt").bufferedReader()

fun main(){
    val assignments: List<Pair<IntRange, IntRange>> = reader.readLines()
        .map { it.split(",") }
        .map { it[0].split("-") to it[1].split("-") }
        .map { p -> p.first.let { it[0].toInt()..it[1].toInt() } to p.second.let { it[0].toInt()..it[1].toInt() } }
    partTwo(assignments)
}

private fun partOne(assignments: List<Pair<IntRange, IntRange>>){
    val fullyContain = assignments.filter {
        it.first.includes(it.second) || it.second.includes(it.first)
    }
    println(fullyContain.size)
}

private fun partTwo(assignments: List<Pair<IntRange, IntRange>>){
    val overlaps = assignments.filter {
        it.first.overlaps(it.second) || it.second.overlaps(it.first)
    }
    println(overlaps.size)

}

private fun IntRange.includes(other: IntRange) = this.first <= other.first && this.last >= other.last
private fun IntRange.overlaps(other: IntRange) = this.first <= other.last && this.last >= other.first