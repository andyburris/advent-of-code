package com.andb.adventofcode.year2019.day5

import com.andb.adventofcode.year2019.common.toIntcode
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2019/day5/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day5/test.txt").bufferedReader()

fun main(){
    partTwo()
}

private fun partOne(){
    val intcode = reader.readLine().split(",").map { it.toInt() }.toIntcode()
    intcode.input = 1
    println(intcode.run())
}

private fun partTwo(){
    val intcode = reader.readLine().split(",").map { it.toInt() }.toIntcode()
    intcode.input = 5
    println(intcode.run())
    println(intcode.size)
    println(intcode[223])
}

private fun testSuite(){
    val comparison1 = testReader.readLine().split(",").map { it.toInt() }.toIntcode()
    val comparison2 = testReader.readLine().split(",").map { it.toInt() }.toIntcode()
    val comparison3 = testReader.readLine().split(",").map { it.toInt() }.toIntcode()
    val comparison4 = testReader.readLine().split(",").map { it.toInt() }.toIntcode()
    val jump1 = testReader.readLine().split(",").map { it.toInt() }.toIntcode()
    val jump2 = testReader.readLine().split(",").map { it.toInt() }.toIntcode()
    val integration = testReader.readLine().split(",").map { it.toInt() }.toIntcode()
    val reddit1 = testReader.readLine().split(",").map { it.toInt() }.toIntcode()
    val reddit2 = testReader.readLine().split(",").map { it.toInt() }.toIntcode()

    comparison1.toIntcode().apply { input = 8; check(run() == 1) }
    comparison1.toIntcode().apply { input = 5; check(run() == 0) }
    comparison1.toIntcode().apply { input = 10; check(run() == 0) }

    comparison2.toIntcode().apply { input = 8; check(run() == 0) }
    comparison2.toIntcode().apply { input = 5; check(run() == 1) }
    comparison2.toIntcode().apply { input = 10; check(run() == 0) }

    comparison3.toIntcode().apply { input = 8; check(run() == 1) }
    comparison3.toIntcode().apply { input = 5; check(run() == 0) }
    comparison3.toIntcode().apply { input = 10; check(run() == 0) }

    comparison4.toIntcode().apply { input = 8; check(run() == 0) }
    comparison4.toIntcode().apply { input = 5; check(run() == 1) }
    comparison4.toIntcode().apply { input = 10; check(run() == 0) }

    jump1.toIntcode().apply { input = 0; check(run() == 0) }
    jump1.toIntcode().apply { input = 1; check(run() == 1) }
    jump1.toIntcode().apply { input = 84; check(run() == 1) }

    jump2.toIntcode().apply { input = 0; check(run() == 0) }
    jump2.toIntcode().apply { input = 1; check(run() == 1) }
    jump2.toIntcode().apply { input = 84; check(run() == 1) }

    integration.toIntcode().apply { input = 8; check(run() == 1000) }
    integration.toIntcode().apply { input = 5; check(run() == 999) }
    integration.toIntcode().apply { input = 7; check(run() == 999) }
    integration.toIntcode().apply { input = 0; check(run() == 999) }
    integration.toIntcode().apply { input = 10; check(run() == 1001) }
    integration.toIntcode().apply { input = 9; check(run() == 1001) }
    integration.toIntcode().apply { input = 84; check(run() == 1001) }

    reddit1.toIntcode().apply { input = 0; check(run() == 0) }
    reddit2.toIntcode().apply { input = 0; check(run() == 0) }

}