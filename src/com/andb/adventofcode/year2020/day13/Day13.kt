package com.andb.adventofcode.year2020.day13

import com.andb.adventofcode.year2020.common.lcm
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2020/day13/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day13/test.txt").bufferedReader()

fun main(){
    partOne()
    //partTwo()
}

private fun partOne(){
    val earliestTime = reader.readLine().toInt()
    val buses = reader.readLine().split(",").filter { it != "x" }.map { it.toInt() }
    val nextTimes = buses.map { (earliestTime / it + 1) * it }
    println(buses)
    println(nextTimes)
    println((nextTimes.min()!! - earliestTime) * buses[nextTimes.indexOf(nextTimes.min()!!)])
}

private fun partTwo(){
    val buses = reader.readLines()[1].split(",").mapIndexed { index, s -> s.toDepartureRule(index.toLong()) }
    val sortedByLeastCommon = buses.filterIsInstance<DepartureRule.ID>()/*.sortedBy { it.id }*/
    println(sortedByLeastCommon.findSequentialTimes())
}

private sealed class DepartureRule {
    abstract val index: Long
    data class NoConstraints(override val index: Long) : DepartureRule()
    data class ID(override val index: Long, val id: Long) : DepartureRule()
}

private fun String.toDepartureRule(index: Long) = when (this) {
    "x" -> DepartureRule.NoConstraints(index)
    else -> DepartureRule.ID(index, this.toLong())
}

private fun List<DepartureRule.ID>.findSequentialTimes(): Long {
    println("testing timestamps = $this")
    var found = -1L
    var index = 0L
    var base = 0L
    val currentBusesFound = mutableListOf(this[0])
    var increment = currentBusesFound[0].id
    while (found < 0) {
        val testTimestamp: Long = base + (increment * index)
        //println("testing timestamp = $testTimestamp")
        val correctTimestamp = this[currentBusesFound.size].timestampIsCorrect(testTimestamp)
        if (correctTimestamp) {
            base = testTimestamp
            currentBusesFound += this[currentBusesFound.size]
            increment = currentBusesFound.map { it.id }.lcm()
            println("found timestamp for ${this[currentBusesFound.size - 1]} at $base, increment = $increment")
            index = 0
        }
        if (currentBusesFound.size == this.size) found = testTimestamp
        index++
    }

    return found
}

private fun List<DepartureRule.ID>.checkTimestamp(timestamp: Long) = this.filter { it.timestampIsCorrect(timestamp) }

private fun List<DepartureRule.ID>.checkTimestampAll(timestamp: Long) = this.all {
    //println("rule = $it")
    val correct = it.timestampIsCorrect(timestamp)
    //println("correct = $correct")
    correct
}
private fun DepartureRule.ID.timestampIsCorrect(startTimestamp: Long) = (startTimestamp + index) % id == 0L
