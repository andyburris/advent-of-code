package com.andb.adventofcode.year2022.day18

import com.andb.adventofcode.year2020.common.tripleCombinations
import java.io.File
import java.lang.Error
import kotlin.math.absoluteValue

private val reader = File("src/com/andb/adventofcode/year2022/day18/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day18/test.txt").bufferedReader()
private val test2Reader = File("src/com/andb/adventofcode/year2022/day18/test2.txt").bufferedReader()

fun main(){
    partTwo()
}

private fun partOne(){
    val cubes = reader.readLines().map { it.toCoordinate3D() }
    val openFaces = cubes.map { cube -> cube.perpendicularNeighbors.count { it !in cubes } }
    println(openFaces)
    println(openFaces.sum())
}

private fun partTwo(){
    val cubes = reader.readLines().map { it.toCoordinate3D() }
    val cubesSpan = cubes.span()
    val emptyCubes = cubesSpan.allCubes.filter { it !in cubes }

    var (outerFacingCubes, remainingCubes) = emptyCubes.partition { it in cubesSpan.border }
    var noMoreOuterFacingCubes = false
    while (!noMoreOuterFacingCubes) {
        val (newOuterFacing, newRemaining) = remainingCubes.partition { rc -> outerFacingCubes.any { it.isTouchingPerpendicular(rc) } }
        if (newOuterFacing.isEmpty()) noMoreOuterFacingCubes = true
        outerFacingCubes += newOuterFacing
        remainingCubes = newRemaining
    }

    val openFaces = cubes.map { cube -> cube.perpendicularNeighbors.count { it !in cubes && it !in remainingCubes } }
    println(openFaces.sum())
}

private fun test(){

}

private data class Coordinate3D(val x: Int, val y: Int, val z: Int) {
    val perpendicularNeighbors get() = (-1..1)
        .toList()
        .tripleCombinations()
        .filter { it.first.absoluteValue + it.second.absoluteValue + it.third.absoluteValue == 1 }
        .map { Coordinate3D(x + it.first, y + it.second, z + it.third) }

    fun isTouchingPerpendicular(other: Coordinate3D) = other in perpendicularNeighbors
}
private fun String.toCoordinate3D() = this
    .split(",")
    .map { it.toInt() }
    .let { Coordinate3D(it[0], it[1], it[2]) }

private data class CubeSpan(
    val xRange: IntRange,
    val yRange: IntRange,
    val zRange: IntRange,
    val allCubes: List<Coordinate3D>
) {
    val border: List<Coordinate3D> by lazy {
        spanOf(xRange.first..xRange.first, yRange, zRange) +
                spanOf(xRange.last..xRange.last, yRange, zRange) +
                spanOf(xRange, yRange.first..yRange.first, zRange) +
                spanOf(xRange, yRange.last..yRange.last, zRange) +
                spanOf(xRange, yRange, zRange.first..zRange.first) +
                spanOf(xRange, yRange, zRange.last..zRange.last)
    }
}
private fun List<Coordinate3D>.span(): CubeSpan {
    val xRange = this.minOf { it.x }..this.maxOf { it.x }
    val yRange = this.minOf { it.y }..this.maxOf { it.y }
    val zRange = this.minOf { it.z }..this.maxOf { it.z }
    val allCubes = spanOf(xRange, yRange, zRange)
    return CubeSpan(xRange, yRange, zRange, allCubes)
}

private fun spanOf(
    xRange: IntRange,
    yRange: IntRange,
    zRange: IntRange,
): List<Coordinate3D> = sequence {
    xRange.forEach { x ->
        yRange.forEach { y ->
            zRange.forEach { z ->
                yield(Coordinate3D(x, y, z))
            }
        }
    }
}.toList()