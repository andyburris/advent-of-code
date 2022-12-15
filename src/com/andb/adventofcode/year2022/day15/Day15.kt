package com.andb.adventofcode.year2022.day15

import com.andb.adventofcode.year2019.common.Coordinate
import com.andb.adventofcode.year2019.common.Direction
import java.io.File
import kotlin.math.absoluteValue

private val reader = File("src/com/andb/adventofcode/year2022/day15/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day15/test.txt").bufferedReader()

fun main(){
//    partOne()
    partTwo()
}

private fun partOne(){
//    val rowToCheck = 10
    val rowToCheck = 2000000

    val sensors = reader.readLines().map { it.toSensor() }
    println(sensors)
    val horizontalRange = (sensors.minOf { it.location.x - it.senseDistance })..(sensors.maxOf { it.location.x + it.senseDistance })
    println(horizontalRange)

    val rowSensed = horizontalRange.filter { x -> sensors.any { it.sensesCoordinate(Coordinate(x, rowToCheck)) } }
    val rowSensedNotBeacon = rowSensed.filter { x -> sensors.none { it.nearestBeacon == Coordinate(x, rowToCheck) } }
    println(rowSensedNotBeacon.size)
}

private fun partTwo(){
//    val rangeToCheck = (0..20)
    val rangeToCheck = (0..4000000)

    val sensors = reader.readLines().map { it.toSensor() }

    val emptyCoords: List<Coordinate?> = rangeToCheck.map { y ->
        val sensorSpans = sensors.map { it.horizontalSenseRangeInRow(y) }.filterNot { it.isEmpty() }.sortedBy { it.first }
        //println("for row $y, sensorSpans = $sensorSpans")
        val hasEmptyCoordinate = sensorSpans.hasGap(rangeToCheck)
        if (!hasEmptyCoordinate) return@map null
        val emptyCoordinate: Coordinate = rangeToCheck
            .first { x ->
                sensors.none { it.sensesCoordinate(Coordinate(x, y)) }
            }
            .let { x -> Coordinate(x, y) }
        emptyCoordinate
    }
    val emptyCoord: Coordinate = emptyCoords.filterNotNull().first()

    println(emptyCoord)
    val tuningFrequency = (emptyCoord.x.toLong() * 4000000L) + emptyCoord.y.toLong()
    println(tuningFrequency)
}

private fun test(){

}

private data class Sensor(val location: Coordinate, val nearestBeacon: Coordinate) {
    val senseDistance = location.manhattanDistanceTo(nearestBeacon)
    fun sensesCoordinate(coordinate: Coordinate) = location.manhattanDistanceTo(coordinate) <= senseDistance

    fun fullyInside(other: Sensor) =
        this.location.x - senseDistance > other.location.x - senseDistance
                && this.location.x + senseDistance < other.location.x + senseDistance
                && this.location.y - senseDistance > other.location.y - senseDistance
                && this.location.y + senseDistance > other.location.y + senseDistance

    fun horizontalSenseRangeInRow(row: Int): IntRange {
        val verticalDiff = (row - this.location.y).absoluteValue
        return (this.location.x - senseDistance + verticalDiff)..(this.location.x + senseDistance - verticalDiff)
    }
}


private fun String.toSensor(): Sensor {
    val (rawSensor, rawBeacon) = this.split(": ")
    val sensorCoordinates = rawSensor.removePrefix("Sensor at x=").split(", y=").map { it.toInt() }.let { Coordinate(it[0], it[1]) }
    val beaconCoordinates = rawBeacon.removePrefix("closest beacon is at x=").split(", y=").map { it.toInt() }.let { Coordinate(it[0], it[1]) }
    return Sensor(sensorCoordinates, beaconCoordinates)
}

private fun List<Sensor>.printable(verticalRange: IntRange, horizontalRange: IntRange, filterSensor: Sensor? = null): String = verticalRange.map { y ->
    val rowString = horizontalRange.map { x ->
        val coord = Coordinate(x, y)
        val c = when {
            coord in this.map { it.location } -> 'S'
            coord in this.map { it.nearestBeacon } -> 'B'
            this.any { (filterSensor == null || filterSensor == it) && it.sensesCoordinate(coord) } -> '#'
            else -> '.'
        }
        c
    }.joinToString("")
    "${y.toString().padStart(3)} $rowString"
}.joinToString("\n")


private fun List<IntRange>.spanSize(filterRange: IntRange? = null): Int = this.fold(0 to emptyList<IntRange>()) { (acc, seenRanges), range ->

    (acc + range.amountNotIn(seenRanges)) to (seenRanges.plusElement(range))
}.first

private fun IntRange.amountNotIn(others: List<IntRange>) = this.count { thisIndex -> others.none { thisIndex in it } }

private fun List<IntRange>.hasGap(filterRange: IntRange): Boolean {
    val allRanges = this
    val noOverlaps = allRanges.filter { thisRange -> allRanges.filter { it != thisRange }.none { it.fullyContains(thisRange) } }
    val sorted = noOverlaps.sortedBy { it.first }
    return sorted.zipWithNext().any { (earlier, later) -> earlier.last < (later.first - 1) }
}

private fun IntRange.fullyContains(other: IntRange) = this.first <= other.first && this.last >= other.last