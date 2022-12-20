package com.andb.adventofcode.year2019.day9

import com.andb.adventofcode.year2019.common.toIntcode
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2019/day9/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day9/test.txt").bufferedReader()

fun main(){
    partTwo()
}

private fun partOne(){
    val software = reader.readLine().split(",").map { it.toLong() }.toIntcode()
    println(software)
    software.run()
    println(software.allOutputs)
}

private fun partTwo(){
    val software = reader.readLine().split(",").map { it.toLong() }.toIntcode()
    software.input = mutableListOf(2)
    println(software)
    software.run()
    println(software.allOutputs)
}

private fun test(){
    val software1 = testReader.readLine().split(",").map { it.toLong() }.toIntcode()
    val software2 = testReader.readLine().split(",").map { it.toLong() }.toIntcode()
    val software3 = testReader.readLine().split(",").map { it.toLong() }.toIntcode()
    for (software in listOf(software1, software2, software3)){
        println(software)
        software.run()
        println(software.allOutputs)
    }
}