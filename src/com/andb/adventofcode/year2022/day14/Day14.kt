package com.andb.adventofcode.year2022.day14

import com.andb.adventofcode.year2019.common.Coordinate
import com.andb.adventofcode.year2019.common.Direction
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2022/day14/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day14/test.txt").bufferedReader()

fun main(){
    //partOne()
    partTwo()
}

private fun partOne(){
    val paths = reader.readLines().map { it.toPath() }
    val startingPoint = Coordinate(500, 0)

    val pathCoords = paths.flatMap { path -> path.zipWithNext().flatMap { it.first.rangeTo(it.second) } }.distinct()

    val stoppedSand = mutableListOf<Coordinate>()
    while (true) {
        val nextSand = startingPoint.nextSandStop(pathCoords, stoppedSand) ?: break
        stoppedSand += nextSand
    }
    println(stoppedSand.size)
}

private fun partTwo(){
    val paths = reader.readLines().map { it.toPath() }
    val startingPoint = Coordinate(500, 0)

    val pathCoords = paths.flatMap { path -> path.zipWithNext().flatMap { it.first.rangeTo(it.second) } }.distinct()

    val stoppedSand = mutableListOf<Coordinate>()
    while (true) {
        val nextSand = startingPoint.nextSandStop2(pathCoords, stoppedSand)
        if (nextSand == startingPoint) break
        stoppedSand += nextSand
    }
    println(stoppedSand.size + 1)
}

private fun test(){

}

typealias Path = List<Coordinate>
private fun String.toPath(): Path = this.split(" -> ").map { s ->
    val (x, y) = s.split(",").map { it.toInt() }
    Coordinate(x, y)
}

private fun Coordinate.nextSandStop(rockPathCoords: List<Coordinate>, stoppedSand: List<Coordinate>): Coordinate? {
    val startingPoint = this
    val allCoordinates = stoppedSand + rockPathCoords
    val fallThreshold = allCoordinates.maxOf { it.y }

    var fallingCoord = startingPoint
    while (fallingCoord.y < fallThreshold) {
        val possibleDown = fallingCoord.move(Direction.NORTH)
        val possibleDownLeft = fallingCoord.move(Direction.NORTH).move(Direction.WEST)
        val possibleDownRight = fallingCoord.move(Direction.NORTH).move(Direction.EAST)
        fallingCoord = when {
            possibleDown !in allCoordinates -> possibleDown
            possibleDownLeft !in allCoordinates -> possibleDownLeft
            possibleDownRight !in allCoordinates -> possibleDownRight
            else -> return fallingCoord
        }
    }
    return null
}

private fun Coordinate.nextSandStop2(rockPathCoords: List<Coordinate>, stoppedSand: List<Coordinate>): Coordinate {
    val startingPoint = this
    val allCoordinates = stoppedSand + rockPathCoords
    val maxRock = rockPathCoords.maxOf { it.y }
    val fallThreshold = maxRock + 2

    var fallingCoord = startingPoint
    while (true) {
        val possibleDown = fallingCoord.move(Direction.NORTH)
        val possibleDownLeft = fallingCoord.move(Direction.NORTH).move(Direction.WEST)
        val possibleDownRight = fallingCoord.move(Direction.NORTH).move(Direction.EAST)
        fallingCoord = when {
            possibleDown !in allCoordinates && possibleDown.y < fallThreshold -> possibleDown
            possibleDownLeft !in allCoordinates && possibleDownLeft.y < fallThreshold -> possibleDownLeft
            possibleDownRight !in allCoordinates && possibleDownRight.y < fallThreshold -> possibleDownRight
            else -> return fallingCoord
        }
    }
}

private fun Coordinate.rangeTo(other: Coordinate): List<Coordinate> {
    val returned = when {
        this.x == other.x -> (this.y to other.y).closedRange().map { Coordinate(this.x, it) }
        this.y == other.y -> (this.x to other.x).closedRange().map { Coordinate(it, this.y) }
        else -> throw Error("$this cannot range to $other")
    }
    return returned
}

private fun Pair<Int, Int>.closedRange() = when {
    this.first <= this.second -> this.first..this.second
    else -> this.second..this.first
}