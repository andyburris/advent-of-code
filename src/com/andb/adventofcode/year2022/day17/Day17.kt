package com.andb.adventofcode.year2022.day17

import com.andb.adventofcode.year2019.common.Coordinate
import com.andb.adventofcode.year2019.common.Direction
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2022/day17/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day17/test.txt").bufferedReader()

fun main(){
    partTwo()
//    test()
}

private fun partOne(){
    val jets = reader.readLine().map { it.toJet() }
    val rocks = listOf(Rock.HorizontalLine(Coordinate(0, 0)), Rock.Plus(Coordinate(0, 0)), Rock.ReverseL(Coordinate(0, 0)), Rock.VerticalLine(Coordinate(0, 0)), Rock.Square(Coordinate(0, 0)))
    val rocksToFall = 2022
    val fallenRocks = simulateRocksFalling(rocks, jets, rocksToFall)
    println(fallenRocks.totalHeight + 1)
}

private fun simulateRocksFalling(
    rocks: List<Rock>,
    jets: List<Jet>,
    rocksToFall: Int,
    initialAcc: FallingRocksAccumulation = FallingRocksAccumulation()
): FallingRocksAccumulation {
//    val rockOffset = initialAcc.floorLayouts.lastOrNull()?.rockIndex ?: 0
    val rockOffset = 0
    val fallenRocks = (0 until rocksToFall).fold(initialAcc) { acc, rockIndex ->
        val rock = rocks[rockIndex % rocks.size] //CHECK: mod is correct
            .withPosition(Coordinate(2, acc.totalHeight + 4))
//        if (rockIndex < 7) println((acc.fallenRocks + rock).printable())
        val (restingRock, stepsTaken) = rock.restingPosition(jets, acc.jetIndex, acc.fallenRocks)
        acc.copy(fallenRocks = acc.fallenRocks + restingRock, rockIndex = rockIndex, jetIndex = acc.jetIndex + stepsTaken)
    }
    return fallenRocks
}

private fun Rock.restingPosition(
    jets: List<Jet>,
    jetIndex: Int,
    fallenRocks: List<Rock>,
    printSteps: Boolean = false
): Pair<Rock, Int> {
    var fallingRock = this
    var stepNumber = 0
    val allFallenCoords = fallenRocks.flatMap { it.currentCoordinates }
    while (true) {
        val nextJet = jets[(jetIndex + stepNumber) % jets.size]
        val possibleHorizontal =
            fallingRock.withPosition(fallingRock.currentPosition.move(nextJet.toDirection()))
        val jetAffects = possibleHorizontal
            .currentCoordinates
            .none { it in allFallenCoords }
                && possibleHorizontal.currentPosition.x.let { 0 <= it && (it + this.width) <= 7 }
        val horizontalStep = if (jetAffects) possibleHorizontal else fallingRock
        val possibleDown = horizontalStep
            .withPosition(horizontalStep.currentPosition.move(Direction.SOUTH))
        val canMoveDown = possibleDown
            .currentCoordinates
            .none { it in allFallenCoords }
                && possibleDown.currentPosition.y >= 0
        fallingRock = when(canMoveDown) {
            true -> possibleDown
            false -> horizontalStep
        }
        stepNumber++
        if (printSteps) println((fallenRocks + fallingRock).printable())
        if (!canMoveDown) break;
    }
    return fallingRock to stepNumber
}

private data class FallingRocksAccumulation(
    val fallenRocks: List<Rock> = emptyList(),
    val rockIndex: Int = 0,
    val jetIndex: Int = 0,
    val floorLayouts: List<FloorLayoutInfo> = emptyList()
) {
    val totalHeight get() = fallenRocks.highestRockCoordinate()
}

private data class FloorLayoutInfo(
    val layout: FloorLayout,
    val jetIndex: Int,
    val rockIndex: Int,
    val totalHeight: Int
) {
    fun mirrors(other: FloorLayoutInfo, jetsSize: Int, rocksSize: Int) = (this.layout == other.layout)
            && (this.jetIndex % jetsSize == other.jetIndex % jetsSize)
            && (this.rockIndex % rocksSize == other.rockIndex % rocksSize)
//            && (this.rockIndex % rocksSize == 0)
}

private fun partTwo(){
    val jets = reader.readLine().map { it.toJet() }
    val rocks = listOf(Rock.HorizontalLine(Coordinate(0, 0)), Rock.Plus(Coordinate(0, 0)), Rock.ReverseL(Coordinate(0, 0)), Rock.VerticalLine(Coordinate(0, 0)), Rock.Square(Coordinate(0, 0)))
//    val rocksToFall = 2022L
    val rocksToFall = 1000000000000L

    var lastFloorLayoutsSize = 0
    val fallenRocks = foldUntil(
        initial = FallingRocksAccumulation(),
        until = { acc ->
            if (acc.floorLayouts.size == lastFloorLayoutsSize) return@foldUntil false
            lastFloorLayoutsSize = acc.floorLayouts.size
            val currentFloor = acc.floorLayouts.last()
            val otherFloors = acc.floorLayouts.dropLast(1)
            val foundDuplicate = currentFloor.layout.size == 2 && otherFloors.any { it.mirrors(currentFloor, jets.size, rocks.size) }
            foundDuplicate
        }
    ) { acc, rockIndex ->
        val rock = rocks[rockIndex % rocks.size] //CHECK: mod is correct
            .withPosition(Coordinate(2, acc.totalHeight + 4)) //CHECK: 3 space or 2 space?
        val (restingRock, stepsTaken) = rock.restingPosition(jets, acc.jetIndex, acc.fallenRocks)
        val newRocks = acc.fallenRocks + restingRock
        val newJetIndex = acc.jetIndex + stepsTaken
        val dropBelowFloor = newRocks.dropAtFloor()
        val newLayout = if (newRocks.size != dropBelowFloor.size) {
            val floorLayout = dropBelowFloor.floorLayout()
//            println(floorLayout.printableFloor())
            listOf(FloorLayoutInfo(floorLayout, newJetIndex, rockIndex, newRocks.highestRockCoordinate()))
        } else listOf()
        acc.copy(fallenRocks = dropBelowFloor, rockIndex = rockIndex, jetIndex = newJetIndex, floorLayouts = acc.floorLayouts.plus(newLayout))
    }
    val secondDuplicateFloor = fallenRocks.floorLayouts.last()
    val firstDuplicateFloor = fallenRocks.floorLayouts.dropLast(1).first { it.mirrors(secondDuplicateFloor, jets.size, rocks.size) }
    val assertFirstHeight = firstDuplicateFloor.totalHeight == simulateRocksFalling(rocks, jets, firstDuplicateFloor.rockIndex).totalHeight
    val assertSecondHeight = secondDuplicateFloor.totalHeight == simulateRocksFalling(rocks, jets, secondDuplicateFloor.rockIndex).totalHeight

    val beginSegmentRocks = firstDuplicateFloor.rockIndex
    val beginSegmentHeight = firstDuplicateFloor.totalHeight
    val correctBeginSegmentHeight = simulateRocksFalling(rocks, jets, firstDuplicateFloor.rockIndex).totalHeight


    val rocksPerSegment = secondDuplicateFloor.rockIndex - firstDuplicateFloor.rockIndex
    val eachSegmentHeight = secondDuplicateFloor.totalHeight - beginSegmentHeight

    val remainingRocksAfterBeginSegment = rocksToFall - beginSegmentRocks
    val totalRepeatedSegments: Long = remainingRocksAfterBeginSegment / rocksPerSegment
    val totalRepeatedRocks = totalRepeatedSegments * rocksPerSegment
    val totalRepeatedHeight = eachSegmentHeight * totalRepeatedSegments
    val totalHeightBeforeEnd = beginSegmentHeight + totalRepeatedHeight

//    val correctBeforeEndHeight = simulateRocksFalling(rocks, jets, beginSegmentRocks + totalRepeatedRocks.toInt()).totalHeight
//    val correctRepeatedHeight = correctBeforeEndHeight - correctBeginSegmentHeight

    val endSegmentRocks = rocksToFall - (beginSegmentRocks + totalRepeatedRocks)
    val lastSegmentRocks = simulateRocksFalling(rocks, jets, fallenRocks.rockIndex + endSegmentRocks.toInt())
    val lastSegmentHeight = lastSegmentRocks.totalHeight - secondDuplicateFloor.totalHeight

//    val correctTotalHeight = simulateRocksFalling(rocks, jets, rocksToFall.toInt()).totalHeight
//    val correctEndSegmentHeight = correctTotalHeight - correctBeforeEndHeight

    val totalHeight = beginSegmentHeight + totalRepeatedHeight + lastSegmentHeight
    println(totalHeight + 1)
}


typealias FloorLayout = List<List<Int>>
private fun List<Rock>.floorLayout(horizontalRange: IntRange = 0 until 7): FloorLayout = when(val highestFilledRow = this.highestFilledRow(horizontalRange)) {
    -1 -> throw Error("floorLayout should only be called after checking that a floor exists")
    else -> {
        val highestRow = this.highestRockCoordinate()
        val allCoordinates = this.flatMap { it.currentCoordinates }
        val floorLayout = (highestFilledRow..highestRow).fold(emptyList<List<Int>>()) { acc, y ->
            val newFilledInThisRow = horizontalRange.filter { x ->
//                Coordinate(x, y) in allCoordinates //&& x !in (acc.lastOrNull() ?: emptyList())
                Coordinate(x, y) in allCoordinates && x !in acc.flatten()
            }
            return@fold acc.plusElement(newFilledInThisRow)
        }
        floorLayout.reversed().dropWhile { it.isEmpty() }
    }
}
private fun List<Rock>.dropAtFloor(): List<Rock> {
    val firstFloor = this.highestFilledRow()
    if (firstFloor == -1) return this
    return this.filter { r -> r.currentCoordinates.any { it.y >= firstFloor } }
}

private fun List<Rock>.highestFilledRow(horizontalRange: IntRange = 0 until 7): Int {
    var currentRow = this.highestRockCoordinate()
    val allCoordinates = this.flatMap { it.currentCoordinates }
    val foundFilledHorizontal = mutableListOf<Int>()
    while(currentRow >= 0) {
        val filledHorizontalCoords = horizontalRange.filter { Coordinate(it, currentRow) in allCoordinates }
        foundFilledHorizontal += filledHorizontalCoords
        val isFilled = horizontalRange.all { it in foundFilledHorizontal }
        if (isFilled) {
            return currentRow
        }
        currentRow--
    }
    return -1
}

private fun List<Rock>.highestRockCoordinate() = this.maxOfOrNull { r -> r.currentCoordinates.maxOf { it.y } } ?: -1

private fun test(){
    val one = listOf(listOf(2), listOf(), listOf(1))
    val two = listOf(listOf(2), listOf(), listOf(1))
    println(one in listOf(two))
}

private enum class Jet {
    Left, Right
}
private fun Jet.toDirection(): Direction = when(this) {
    Jet.Left -> Direction.WEST
    Jet.Right -> Direction.EAST
}

private fun Char.toJet() = when(this) {
    '<' -> Jet.Left
    '>' -> Jet.Right
    else -> throw Error("$this is invalid Jet")
}

private sealed class Rock(
    val relativeCoordinates: List<Coordinate>,
) {
    abstract val currentPosition: Coordinate
    val currentCoordinates: List<Coordinate> by lazy { relativeCoordinates.map {
        Coordinate(it.x + currentPosition.x, it.y + currentPosition.y)
//        it + currentPosition
    } }
    val width = relativeCoordinates.distinctBy { it.x }.size
    private constructor(vararg pairs: Pair<Int, Int>) : this(pairs.map { Coordinate(it.first, it.second) })
    data class HorizontalLine(override val currentPosition: Coordinate) : Rock(0 to 0, 1 to 0, 2 to 0, 3 to 0)
    data class Plus(override val currentPosition: Coordinate) : Rock(0 to 1, 1 to 0, 1 to 1, 1 to 2, 2 to 1)
    data class ReverseL(override val currentPosition: Coordinate) : Rock(0 to 0, 1 to 0, 2 to 0, 2 to 1, 2 to 2)
    data class VerticalLine(override val currentPosition: Coordinate) : Rock(0 to 0, 0 to 1, 0 to 2, 0 to 3)
    data class Square(override val currentPosition: Coordinate) : Rock(0 to 0, 0 to 1, 1 to 0, 1 to 1)

    fun withPosition(coordinate: Coordinate) = when(this) {
        is HorizontalLine -> this.copy(currentPosition = coordinate)
        is Plus -> this.copy(currentPosition = coordinate)
        is ReverseL -> this.copy(currentPosition = coordinate)
        is Square -> this.copy(currentPosition = coordinate)
        is VerticalLine -> this.copy(currentPosition = coordinate)
    }
}

private fun List<Rock>.printable() = buildString {
    val widthRange = 0 until 7
    val allCoordinates = this@printable.flatMap { it.currentCoordinates }
    val heightRange = 0 .. (allCoordinates.maxOfOrNull { it.y } ?: 0)

    appendLine()
    heightRange.reversed().map { y ->
        append('|')
        widthRange.map { x ->
            append(if (Coordinate(x,y) in allCoordinates) '#' else '.')
        }
        append('|')
        appendLine()
    }
    append('+')
    repeat(7) { append('-') }
    append('+')
}

private fun FloorLayout.printableFloor() = buildString {
    val widthRange = 0 until 7

    this@printableFloor.reversed().map { row ->
        widthRange.map { x ->
            append(if (x in row) '#' else '.')
        }
        appendLine()
    }
    appendLine()
}

private fun <R> foldWhile(initial: R, runWhile: (R) -> Boolean, transform: (R, Int) -> R): R = foldUntil(initial, { !runWhile(it) }, transform)
private fun <R> foldUntil(initial: R, until: (R) -> Boolean, transform: (R, Int) -> R): R {
    var acc = initial
    var index = 0
    while (!until.invoke(acc)) {
        acc = transform(acc, index)
        index++
    }
    return acc
}