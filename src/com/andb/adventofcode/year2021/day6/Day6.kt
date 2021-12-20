package com.andb.adventofcode.year2021.day6

import com.andb.adventofcode.year2019.common.pow
import com.andb.adventofcode.year2019.common.sumByLong
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2021/day6/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2021/day6/test.txt").bufferedReader()

fun main(){
    partTwo()
}

private fun partOne(){
    val lanternfish = reader.readLine().split(",").map { it.toInt() }
    val after80Days = (0 until 80).fold(lanternfish) { oldFish, _ ->
        val newTimers = oldFish.map { it - 1 }
        val withNewFish = newTimers + newTimers.filter { it == -1 }.map { 8 }
        val withResetTimers = withNewFish.map { if (it == -1) 6 else it }
        withResetTimers
    }
    println(after80Days.size)
}

private fun partTwo(){
    val lanternfish = reader.readLine().split(",").map { it.toInt() }
    val grouped = lanternfish.groupBy { it }.mapValues { it.value.size.toLong() }
    val schoolSize = (0 until 256).fold(grouped) { groups, i ->
        val newTimers = groups.mapKeys { it.key - 1 }
        val withNewFish = newTimers + mapOf(8 to (groups[0] ?: 0))
        val withResetTimers = withNewFish
            .filterKeys { it >= 0 } +
                (6 to (withNewFish.getOrDefault(6, 0) + withNewFish.getOrDefault(-1, 0)))
        withResetTimers
    }.sumByLong { it.value }
    println(schoolSize)
}

private fun test(){

}