package com.andb.adventofcode.year2019.day3

import java.io.File
import kotlin.math.absoluteValue

private val reader = File("src/com/andb/adventofcode/year2019/day3/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day3/test.txt").bufferedReader()

fun main(){
    val firstWire = reader.readLine().split(",")
    val secondWire = reader.readLine().split(",")

    val firstPath = Path(firstWire.map { it.toPathComponent() })
    val secondPath = Path(secondWire.map { it.toPathComponent() })

    partTwo(firstPath, secondPath)
}

private fun partOne(firstPath: Path, secondPath: Path){
    val intersects = firstPath.intersects(secondPath)
    val min = intersects.map { it.x.absoluteValue + it.y.absoluteValue }.minBy { it } ?: -1
    println("Total Manhattan distance: $min")
}

private fun partTwo(firstPath: Path, secondPath: Path){
    val intersects = firstPath.intersectsTimed(secondPath)
    val min = intersects.map { it.first.absoluteValue }.minBy { it } ?: -1
    println("Total Manhattan distance: $min")
}