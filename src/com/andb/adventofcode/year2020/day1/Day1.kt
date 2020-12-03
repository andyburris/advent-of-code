package com.andb.adventofcode.year2020.day1

import com.andb.adventofcode.year2020.common.pairCombinations
import com.andb.adventofcode.year2020.common.tripleCombinations
import java.io.File
import kotlin.streams.toList

private val reader = File("src/com/andb/adventofcode/year2020/day1/input.txt").bufferedReader()
//private val testReader = File("src/com/andb/adventofcode/year2020/day1/test.txt").bufferedReader()

fun main(){
    partTwo()
}

private fun partOne(){
    val numbers = reader.lines().map { it.toInt() }.toList()
    println(numbers.pairCombinations().first { it.first + it.second == 2020 }.let { it.first * it.second })
}

private fun partTwo(){
    val numbers = reader.lines().map { it.toInt() }.toList()
    println(numbers.tripleCombinations().first { it.first + it.second + it.third == 2020 }.let { it.first * it.second * it.third })
}

private fun test(){

}