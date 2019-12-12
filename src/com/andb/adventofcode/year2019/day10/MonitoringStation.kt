package com.andb.adventofcode.year2019.day10

import com.andb.adventofcode.year2019.common.pow
import com.andb.adventofcode.year2019.common.removeAll
import java.io.File
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sqrt

private val reader = File("src/com/andb/adventofcode/year2019/day10/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day10/test.txt").bufferedReader()

fun main() {
    //partOne()
    partTwo()
}

private fun partOne() {
    val asteroidBelt: List<Asteroid> = reader.readLines()
        .mapIndexed { index, line -> line.toCharArray().mapIndexed { cIndex, c -> if(c == '#') Asteroid(cIndex, index) else null}.filterNotNull() }
        .flatten()

    val detectionPairs = asteroidBelt.map { possiblity-> Pair(possiblity, asteroidBelt.groupBy { possiblity.angleTo(it) }.size - 1) }.sortedBy { it.first.x }.sortedBy { it.first.y }
    println(detectionPairs)
    val best = detectionPairs.maxBy { it.second }
    println(best)
}

private fun partTwo(){
    val asteroidBelt: List<Asteroid> = reader.readLines()
        .mapIndexed { index, line -> line.toCharArray().mapIndexed { cIndex, c -> if(c == '#') Asteroid(cIndex, index) else null}.filterNotNull() }
        .flatten()

    val base = asteroidBelt.first { it == Asteroid(25, 31) }
    val detections = asteroidBelt
        .groupBy { base.angleTo(it) }
        .mapKeys { val newKey = (it.key - 90); if (newKey <= 0) newKey+360 else newKey }
        .toSortedMap(Comparator.reverseOrder())
        .mapValues { entry-> entry.value.sortedByDescending { it.proximityTo(base) }.toMutableList() }
        .filter { !it.key.isNaN() }
        .toMutableMap()
    println(detections)
    val sequenceOfDestruction = mutableListOf<Asteroid>()
    while (detections.isNotEmpty()){
        val toRemove = mutableListOf<Double>()
        for (e in detections){
            val destroyed = e.value.first()
            sequenceOfDestruction.add(destroyed)
            e.value.remove(destroyed)
            if(e.value.isEmpty()){
                toRemove.add(e.key)
            }
        }
        detections.removeAll(toRemove)
    }
    println(sequenceOfDestruction)
    val at200 = sequenceOfDestruction[199]
    println("0 = ${sequenceOfDestruction[0]}")
    println("1 = ${sequenceOfDestruction[1]}")
    println("2 = ${sequenceOfDestruction[2]}")
    println("10 = ${sequenceOfDestruction[9]}")
    println("20 = ${sequenceOfDestruction[19]}")
    println("50 = ${sequenceOfDestruction[49]}")
    println("100 = ${sequenceOfDestruction[99]}")
    println("199 = ${sequenceOfDestruction[198]}")
    println("200 = ${sequenceOfDestruction[199]}")
    println("201 = ${sequenceOfDestruction[200]}")
    println("299 = ${sequenceOfDestruction[298]}")
    println(at200)
    println(at200.x * 100 + at200.y)
}

private data class Asteroid(val x: Int, val y: Int){
    /**
     * @param other asteroid to measure slope to
     * @return Angle to asteroid
     */
    fun angleTo(other: Asteroid): Double{
        val x = (other.x - this.x).toDouble()
        val y = (this.y - other.y).toDouble()
        val angle = Math.toDegrees(atan(y/x))
        val quadrant = when{
            other.x >= this.x && other.y < this.y -> 1
            other.x < this.x && other.y < this.y -> 2
            other.x < this.x && other.y >= this.y -> 3
            else -> 4
        }
        return angle + if (quadrant in listOf(2, 3)) 180 else 0
    }

    fun proximityTo(other: Asteroid) = sqrt(x.pow(2) + y.pow(2))

    override fun toString(): String {
        return "Asteroid($x, $y)"
    }
}