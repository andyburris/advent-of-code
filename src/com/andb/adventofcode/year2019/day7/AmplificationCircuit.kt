package com.andb.adventofcode.year2019.day7

import com.andb.adventofcode.year2019.common.*
import kotlinx.coroutines.*
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2019/day7/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day7/test.txt").bufferedReader()

fun main(): Unit = runBlocking {
    launch { partTwo() }
    return@runBlocking
}

private fun partOne() {
    val software = reader.readLine().split(",").map { it.toInt() }.toIntcode()
    val outputs = mutableListOf<Long>()
    val allPhaseSettings = (0..4).toList().allShuffles()
    println("allPhaseSettings: $allPhaseSettings")

    for (phaseSetting in allPhaseSettings) {
        outputs.add(runAmplifiers(software, phaseSetting))
    }
    println(outputs.maxBy { it })
}

private suspend fun partTwo() {
    val software = reader.readLine().split(",").map { it.toInt() }.toIntcode()
    val outputs = mutableListOf<Int>()
    val allPhaseSettings = (5..9).toList().allShuffles()
    val jobs = mutableListOf<Deferred<Long>>()

    for (phaseSetting in allPhaseSettings) {
        jobs.add(CoroutineScope(Dispatchers.Default).async {
            return@async runAmplifiersAsync(software, phaseSetting)
        })
    }

    println(jobs.awaitAll().maxBy { it })
}

private fun runAmplifiers(software: Intcode, phaseSettings: List<Int>): Long {
    val amplifier1 = software.clone()
    val amplifier2 = software.clone()
    val amplifier3 = software.clone()
    val amplifier4 = software.clone()
    val amplifier5 = software.clone()

    amplifier1.input = mutableListOf(phaseSettings[0].toLong(), 0)
    amplifier1.run()
    amplifier2.input = mutableListOf(phaseSettings[1].toLong(), amplifier1.output)
    amplifier2.run()
    amplifier3.input = mutableListOf(phaseSettings[2].toLong(), amplifier2.output)
    amplifier3.run()
    amplifier4.input = mutableListOf(phaseSettings[3].toLong(), amplifier3.output)
    amplifier4.run()
    amplifier5.input = mutableListOf(phaseSettings[4].toLong(), amplifier4.output)
    amplifier5.run()

    return amplifier5.output
}

private suspend fun runAmplifiersAsync(software: Intcode, phaseSettings: List<Int>): Long {
    val amplifier1 = software.toAmplifier(1)
    val amplifier2 = software.toAmplifier(2)
    val amplifier3 = software.toAmplifier(3)
    val amplifier4 = software.toAmplifier(4)
    val amplifier5 = software.toAmplifier(5)

    amplifier1.nextLink = amplifier2
    amplifier2.nextLink = amplifier3
    amplifier3.nextLink = amplifier4
    amplifier4.nextLink = amplifier5
    amplifier5.nextLink = amplifier1

    amplifier1.input = mutableListOf(phaseSettings[0].toLong(), 0)
    amplifier2.input = mutableListOf(phaseSettings[1].toLong())
    amplifier3.input = mutableListOf(phaseSettings[2].toLong())
    amplifier4.input = mutableListOf(phaseSettings[3].toLong())
    amplifier5.input = mutableListOf(phaseSettings[4].toLong())

    newIOThread { amplifier1.run() }
    newIOThread { amplifier2.run() }
    newIOThread { amplifier3.run() }
    newIOThread { amplifier4.run() }
    val job = CoroutineScope(Dispatchers.IO).async {
        amplifier5.run()
        return@async amplifier5.output
    }
    return job.await()
}

private fun test() {
    val software1 = testReader.readLine().split(",").map { it.toInt() }.toIntcode()
    val software2 = testReader.readLine().split(",").map { it.toInt() }.toIntcode()
    val software3 = testReader.readLine().split(",").map { it.toInt() }.toIntcode()

    val phaseSetting1 = listOf(4, 3, 2, 1, 0)
    check(runAmplifiers(software1, phaseSetting1) == 43210L)

    val phaseSetting2 = listOf(0, 1, 2, 3, 4)
    check(runAmplifiers(software2, phaseSetting2) == 54321L)

    val phaseSetting3 = listOf(1, 0, 4, 3, 2)
    check(runAmplifiers(software3, phaseSetting3) == 65210L)
}

private suspend fun testTwo(){
    val software1 = testReader.readLine().split(",").map { it.toInt() }.toIntcode()

    val phaseSettings1 = listOf(9,8,7,6,5)
    check(runAmplifiersAsync(software1, phaseSettings1) == 139629729L)
}


private fun <T> List<T>.allShuffles(): List<List<T>> {
    val possibilites = size.factorial()
    val output = mutableListOf<List<T>>()
    for (i in 0 until possibilites) {
        var possible = this.shuffled()
        while (possible in output) {
            possible = this.shuffled()
        }
        output.add(possible)
    }
    return output
}

private fun Int.factorial(): Int {
    var sum = 1
    (1..this).forEach { sum *= it }
    return sum
}