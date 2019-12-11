package com.andb.adventofcode.year2019.day5

import com.andb.adventofcode.year2019.common.clone
import com.andb.adventofcode.year2019.common.toIntcode
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2019/day5/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day5/test.txt").bufferedReader()

fun main(){
    partTwo()
}

private fun partOne(){
    val intcode = reader.readLine().split(",").map { it.toInt() }.toIntcode()
    intcode.input = mutableListOf(1)
    println(intcode.run())
}

private fun partTwo(){
    val intcode = reader.readLine().split(",").map { it.toInt() }.toIntcode()
    intcode.input = mutableListOf(5)
    println(intcode.run())
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

    comparison1.clone().apply { input = mutableListOf(8); check(run() == 1L) }
    comparison1.clone().apply { input = mutableListOf(5); check(run() == 0L) }
    comparison1.clone().apply { input = mutableListOf(10); check(run() == 0L) }

    comparison2.clone().apply { input = mutableListOf(8); check(run() == 0L) }
    comparison2.clone().apply { input = mutableListOf(5); check(run() == 1L) }
    comparison2.clone().apply { input = mutableListOf(10); check(run() == 0L) }

    comparison3.clone().apply { input = mutableListOf(8); check(run() == 1L) }
    comparison3.clone().apply { input = mutableListOf(5); check(run() == 0L) }
    comparison3.clone().apply { input = mutableListOf(10); check(run() == 0L) }

    comparison4.clone().apply { input = mutableListOf(8); check(run() == 0L) }
    comparison4.clone().apply { input = mutableListOf(5); check(run() == 1L) }
    comparison4.clone().apply { input = mutableListOf(10); check(run() == 0L) }

    jump1.clone().apply { input = mutableListOf(0); check(run() == 0L) }
    jump1.clone().apply { input = mutableListOf(1); check(run() == 1L) }
    jump1.clone().apply { input = mutableListOf(84); check(run() == 1L) }

    jump2.clone().apply { input = mutableListOf(0); check(run() == 0L) }
    jump2.clone().apply { input = mutableListOf(1); check(run() == 1L) }
    jump2.clone().apply { input = mutableListOf(84); check(run() == 1L) }

    integration.clone().apply { input = mutableListOf(8); check(run() == 1000L) }
    integration.clone().apply { input = mutableListOf(5); check(run() == 999L) }
    integration.clone().apply { input = mutableListOf(7); check(run() == 999L) }
    integration.clone().apply { input = mutableListOf(0); check(run() == 999L) }
    integration.clone().apply { input = mutableListOf(10); check(run() == 1001L) }
    integration.clone().apply { input = mutableListOf(9); check(run() == 1001L) }
    integration.clone().apply { input = mutableListOf(84); check(run() == 1001L) }

    reddit1.clone().apply { input = mutableListOf(0); check(run() == 0L) }
    reddit2.clone().apply { input = mutableListOf(0); check(run() == 0L) }

}