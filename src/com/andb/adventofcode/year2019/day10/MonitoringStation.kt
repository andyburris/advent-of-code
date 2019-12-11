package com.andb.adventofcode.year2019.day10

import java.io.File

private val reader = File("src/com/andb/adventofcode/year2019/day10/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day10/test.txt").bufferedReader()

fun main() {
    partOne()
}

private fun partOne() {
    val asteroidBelt = testReader.readLines()
        .mapIndexed { index, line -> line.toCharArray().mapIndexed { cIndex, c -> Space(cIndex, index, c == '#') } }
        .flatten()

    val best = asteroidBelt.filter { it.asteroid }.maxBy { possiblity-> asteroidBelt.groupBy { possiblity.slopeTo(it) }.size }
    println(best)
}

private data class Space(val x: Int, val y: Int, val asteroid: Boolean){
    fun slopeTo(other: Space) = (other.y - this.y).toDouble()/(other.x - this.x).toDouble()
}