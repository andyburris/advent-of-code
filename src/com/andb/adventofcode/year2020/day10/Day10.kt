package com.andb.adventofcode.year2020.day10

import java.io.File
import kotlin.time.seconds

private val reader = File("src/com/andb/adventofcode/year2020/day10/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day10/test.txt").bufferedReader()

fun main(){
    //partOne()
    partTwo()
}

private fun partOne(){
    val bagAdapters = reader.readLines().map { it.toInt() }
    val endAdapter = bagAdapters.max()!! + 3
    println(endAdapter)

    val allAdapters: List<Int> = (bagAdapters + endAdapter).sorted()

    val differences = allAdapters.fold(listOf(0)) { acc, i -> acc + (i - (allAdapters.getOrNull(acc.size - 2) ?: 0)) }
    println(differences.groupBy { it }.getValue(3).size * differences.groupBy { it }.getValue(1).size)
}

private fun partTwo(){
    val bagAdapters = reader.readLines().map { it.toLong() }
    val endAdapter = bagAdapters.max()!! + 3
    val allAdapters: List<Long> = (bagAdapters + endAdapter).sorted()
    println((listOf(0L) + allAdapters).ascendingCombinations())
}

fun List<Long>.ascendingCombinations(): Long {
    val paths = mutableMapOf(0L to 1L)
    this.forEach {
        paths[it + 1] = (paths[it + 1] ?: 0) + paths[it]!!
        paths[it + 2] = (paths[it + 2] ?: 0) + paths[it]!!
        paths[it + 3] = (paths[it + 3] ?: 0) + paths[it]!!
    }
    return paths[this.max()!! + 3]!!
}