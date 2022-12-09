package com.andb.adventofcode.year2022.day9

import com.andb.adventofcode.year2019.common.Coordinate
import com.andb.adventofcode.year2019.common.Direction
import java.io.File
import java.lang.Error

private val reader = File("src/com/andb/adventofcode/year2022/day9/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day9/test.txt").bufferedReader()
private val test2Reader = File("src/com/andb/adventofcode/year2022/day9/test2.txt").bufferedReader()

fun main(){
    //partOne()
    partTwo()
}

private fun partOne(){
    val steps: List<Direction> = reader.readLines()
        .map { raw -> List(raw.trim().takeLastWhile { it in '0'..'9' }.toInt()) { raw.first().toDirection() } }
        .flatten()
    val visited = steps.fold(RopeInfo(listOf(Coordinate(0, 0)), Coordinate(0, 0), Coordinate(0,0))) { acc, step ->
        val newHead = acc.headCoordinate.move(step)
        if (newHead.isAdjacentTo(acc.tailCoordinate)) return@fold acc.copy(headCoordinate = newHead)
        if (newHead.isParallelTo(acc.tailCoordinate)) return@fold acc.copy(headCoordinate = newHead, tailCoordinate = acc.tailCoordinate.move(step), visitedTailCoordinates = acc.visitedTailCoordinates + acc.tailCoordinate.move(step))

        val newTail = acc.tailCoordinate.copy(
            x = if (newHead.x > acc.tailCoordinate.x) acc.tailCoordinate.x + 1 else acc.tailCoordinate.x - 1,
            y = if (newHead.y > acc.tailCoordinate.y) acc.tailCoordinate.y + 1 else acc.tailCoordinate.y - 1,
        )
        RopeInfo(visitedTailCoordinates = acc.visitedTailCoordinates + newTail, headCoordinate = newHead, tailCoordinate = newTail)
    }.visitedTailCoordinates
    println(visited.distinct().size)
}

private val printableRange = -15 until 16
private fun partTwo(){
    val steps: List<Direction> = reader.readLines()
        .map { raw -> List(raw.trim().takeLastWhile { it in '0'..'9' }.toInt()) { raw.first().toDirection() } }
        .flatten()
    val initialInfo = SnappedRopeInfo(listOf(Coordinate(0, 0)), (0..9).map { Coordinate(0, 0) })
    val initialPrintable = initialInfo.knotsCoordinates.printable(printableRange, printableRange)
    val finalInfo = steps.fold<Direction, Pair<SnappedRopeInfo, Pair<String?, Direction?>>>(initialInfo to (initialPrintable to null)) { (acc, lastStepInfo), step ->
        val newHeadReal = acc.knotsCoordinates.first().move(step)
        val newKnots = acc.knotsCoordinates.drop(1).fold(newHeadReal to listOf(newHeadReal)) { (last, acc), current ->
            if (last.isAdjacentTo(current)) return@fold current to acc + current
            if (last.isParallelTo(current)) {
                val movement = when {
                    current.x < last.x -> Direction.EAST
                    current.x > last.x -> Direction.WEST
                    current.y < last.y -> Direction.NORTH
                    current.y > last.y -> Direction.SOUTH
                    else -> throw Error("should be adjacent")
                }
                return@fold current.move(movement) to (acc + current.move(movement))
            }

            val newCurrent = current.copy(
                x = if (last.x > current.x) current.x + 1 else current.x - 1,
                y = if (last.y > current.y) current.y + 1 else current.y - 1,
            )
            if (!newCurrent.isAdjacentTo(last)) throw Error("NOT ADJACENT! newCurrent = $newCurrent, last = $last, head = $newHeadReal, step = $step, lastStep = ${lastStepInfo.second}, printable = ${lastStepInfo.first}")
            newCurrent to acc + newCurrent
        }.second
        if (step != lastStepInfo.second) {
            //println(lastStepInfo.first)
        }
        SnappedRopeInfo(visitedTailCoordinates = acc.visitedTailCoordinates + newKnots.last(), knotsCoordinates = newKnots) to (newKnots.printable(printableRange, printableRange) to step)
    }.first
    println(finalInfo.knotsCoordinates.printable(printableRange, printableRange))
    println(finalInfo.visitedTailCoordinates.distinct().size)
}

private fun test(){

}

private data class RopeInfo(val visitedTailCoordinates: List<Coordinate>, val headCoordinate: Coordinate, val tailCoordinate: Coordinate)
private data class SnappedRopeInfo(val visitedTailCoordinates: List<Coordinate>, val knotsCoordinates: List<Coordinate>)


private fun Char.toDirection() = when(this) {
    'R' -> Direction.EAST
    'U' -> Direction.NORTH
    'L' -> Direction.WEST
    'D' -> Direction.SOUTH
    else -> throw Error("$this is invalid")
}

fun Coordinate.isAdjacentTo(other: Coordinate) = other.x in (this.x - 1)..(this.x + 1) && other.y in (this.y - 1)..(this.y + 1)
fun Coordinate.isParallelTo(other: Coordinate) = this.x == other.x || this.y == other.y


private fun List<Coordinate>.printable(xRange: IntRange, yRange: IntRange): String = buildString {
    yRange.reversed().map { y ->
        xRange.map { x ->
            val lowestIndexAtCoordinate = this@printable.indexOfFirst { it == Coordinate(x, y) }
            when(lowestIndexAtCoordinate) {
                -1 -> append('.')
                0 -> append('H')
                //this@printable.size - 1 -> append('T')
                else -> append(lowestIndexAtCoordinate.digitToChar())
            }
        }
        append('\n')
    }
}