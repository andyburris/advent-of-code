package com.andb.adventofcode.year2019.day2

import com.andb.adventofcode.year2019.common.toIntcode
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2019/day2/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day2/test.txt").bufferedReader()

fun main() {
    partTwo()
}

private fun partOne() {
    val intcode = reader.readLine().split(",").map { it.toInt() }.toIntcode()
    intcode.inputIntoCode(12, 2)
    intcode.run()
    println(intcode.outputFromCode())
}

private val DESIRED_OUTPUT = 19690720
private fun partTwo() {
    val intcode = reader.readLine().split(",").map { it.toInt() }.toIntcode()
    var finalNoun = -1
    var finalVerb = -1
    for (noun in 0..99) {
        for (verb in 0..99) {
            val clone = intcode.toIntcode()
            clone.inputIntoCode(noun, verb)
            clone.run()
            if (clone.outputFromCode() == DESIRED_OUTPUT) {
                finalNoun = noun
                finalVerb = verb
            }
        }
    }
    println("noun: $finalNoun")
    println("verb: $finalVerb")
    println("answer: ${100 * finalNoun + finalVerb}")
}