package com.andb.adventofcode.year2021.day9

import com.andb.adventofcode.Point
import com.andb.adventofcode.year2019.common.sumBy
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2021/day9/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2021/day9/test.txt").bufferedReader()

fun main(){
    val coordinateMap = reader
        .readLines()
        .map { line -> line.toList().map { it.toString().toInt() } }
        .toCoordinateMap()
    partTwo(coordinateMap)
}

private fun partOne(heightMap: CoordinateMap<Int>){
    println(heightMap.lowPoints().values.sumBy { 1 + it })
}

private fun partTwo(heightMap: CoordinateMap<Int>){
    val lowPoints = heightMap.lowPoints()
    val basins = lowPoints.map { heightMap.buildBasinFrom(it.key) }
    val answer = basins
        .sortedByDescending { it.size }
        .take(3)
        .fold(1) { acc, basin -> acc * basin.size }
    println(answer)
}

private fun CoordinateMap<Int>.buildBasinFrom(lowPoint: Point) = this.addToBasinFrom(lowPoint, listOf(lowPoint))

private fun CoordinateMap<Int>.addToBasinFrom(point: Point, builtBasin: List<Point>): List<Point> {
    val newPoints = this.adjacent(point).filter { it.value != 9 && it.key !in builtBasin }.keys.toList()
    return newPoints.fold(builtBasin + newPoints) { basin, newPoint ->
        addToBasinFrom(newPoint, basin)
    }
}

private fun test(){

}

private fun CoordinateMap<Int>.lowPoints() = this.filter { (coord, value) ->
    val surrounding = this.adjacentValues(coord)
    surrounding.all { value < it }
}
private fun <T> CoordinateMap<T>.adjacent(point: Point): Map<Point, T> {
    val (x, y) = point
    return mapOf(
        Point(x - 1, y) to this.valueAt(x - 1, y),
        Point(x + 1, y) to this.valueAt(x + 1, y),
        Point(x, y - 1) to this.valueAt(x, y - 1),
        Point(x, y + 1) to this.valueAt(x, y + 1),
    )
        .filterValues { it != null }
        .mapValues { it.value!! }
}

private fun <T> CoordinateMap<T>.adjacentValues(point: Point): List<T> {
    val (x, y) = point
    return listOfNotNull(
        this.valueAt(x - 1, y),
        this.valueAt(x + 1, y),
        this.valueAt(x, y - 1),
        this.valueAt(x, y + 1),
    )
}

typealias CoordinateMap<T> = Map<Point, T>
fun <T> CoordinateMap<T>.valueAt(x: Int, y: Int) = this.valueAt(Point(x, y))
fun <T> CoordinateMap<T>.valueAt(point: Point) = this[point]
fun <T> CoordinateMap<T>.getValueAt(x: Int, y: Int) = this.getValue(Point(x, y))
fun <T> CoordinateMap<T>.getValueAt(point: Point) = this.getValue(point)
fun <T> List<List<T>>.toCoordinateMap(): CoordinateMap<T> = this.mapIndexed { y: Int, row: List<T> ->
    row.mapIndexed { x: Int, value: T ->
        Point(x, y) to value
    }
}.flatten().toMap()