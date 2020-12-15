package com.andb.adventofcode.year2020.day13

import com.andb.adventofcode.year2020.common.lcm
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2020/day13/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day13/test.txt").bufferedReader()

fun main(){
    //partOne()
    partTwo()
}

private fun partOne(){
    val earliestTime = reader.readLine().toInt()
    val buses = reader.readLine().split(",").filter { it != "x" }.map { it.toInt() }
    val nextTimes = buses.map { (earliestTime / it + 1) * it }
    println((nextTimes.min()!! - earliestTime) * buses[nextTimes.indexOf(nextTimes.min()!!)])
}

private fun partTwo(){
    val buses = reader.readLines()[1].split(",").mapIndexed { index, s -> IndexedValue(index, s) }.filter { it.value != "x" }.map { IndexedValue(it.index, it.value.toLong()) }
    println(buses.findSequentialTimes())
}

private fun List<IndexedValue<Long>>.findSequentialTimes(): Long = this
    .drop(1)
    .fold(Pair(0L, this.first().value)) { (time, increment), bus ->
        var searchTime = time
        while (true) {
            if (bus.timestampIsCorrect(searchTime)) {
                return@fold Pair(searchTime, increment * bus.value)
            }
            searchTime += increment
        }
        return@fold throw Error("Can't reach")
    }.first

private fun IndexedValue<Long>.timestampIsCorrect(startTimestamp: Long) = (startTimestamp + this.index) % this.value == 0L
