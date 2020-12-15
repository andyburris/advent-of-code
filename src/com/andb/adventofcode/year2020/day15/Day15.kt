package com.andb.adventofcode.year2020.day15

import java.io.File
import kotlin.test.assertEquals

private val reader = File("src/com/andb/adventofcode/year2020/day15/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day15/test.txt").bufferedReader()

fun main(){
    partOne()
    //test()
    //partTwo()
}

private fun partOne(){
    val startingNumbers = reader.readLine().split(",").map { it.toInt() }
    val said = startingNumbers.dropLast(1).mapIndexed { index: Int, i: Int -> i to index }.toMap().toMutableMap()
    println(said)
    var index = startingNumbers.size
    var last = startingNumbers.last()
    repeat(30000000 - index) {
        val next = if (said.containsKey(last)) index - said[last]!! - 1 else 0
        println("turn $index = $next")
        said[last] = index - 1
        index++
        last = next
    }
    println(last)
}

private fun partTwo(){

}