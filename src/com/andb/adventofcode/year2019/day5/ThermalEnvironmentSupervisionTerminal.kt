package com.andb.adventofcode.year2019.day5

import com.andb.adventofcode.year2019.common.extendTo
import com.andb.adventofcode.year2019.common.toDigits
import com.andb.adventofcode.year2019.common.toIntcode
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2019/day5/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day5/test.txt").bufferedReader()

fun main(){
    partTwo()
}

fun partOne(){
    val intcode = reader.readLine().split(",").map { it.toInt() }.toIntcode()
    intcode.input = 1
    println(intcode.run())
}

fun partTwo(){
    val intcode = testReader.readLine().split(",").map { it.toInt() }.toIntcode()
    intcode.input = 7
    println(intcode.run())
}

fun test(){
    println(listOf(0, 1).extendTo(3))
}