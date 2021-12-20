package com.andb.adventofcode.year2021.day5

import com.andb.adventofcode.Point
import com.andb.adventofcode.rangeTo
import com.andb.adventofcode.year2019.common.Coordinate
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2021/day5/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2021/day5/test.txt").bufferedReader()

fun main(){
    partTwo()
}

private fun partOne(){
    val lines = reader.readLines().map { it.toLine() }
    val overlappingPoints = lines
        .filter { it.isHorizontal() || it.isVertical() }
        .map { it.points() }
        .flatten()
        .groupBy { it }
        .filterValues { it.size >= 2 }
        .count()
    println(overlappingPoints)
}

private fun partTwo(){
    val lines = reader.readLines().map { it.toLine() }
    val overlappingPoints = lines
        .map { it.points() }
        .flatten()
        .groupBy { it }
        .filterValues { it.size >= 2 }
        .count()
    println(overlappingPoints)
}

private fun test(){

}

data class Line(val start: Point, val end: Point)
fun Line.points(): List<Point> = when {
    this.isHorizontal() -> (start.x rangeTo end.x).toList().map { x -> Point(x, start.y) }
    this.isVertical() -> (start.y rangeTo end.y).toList().map { y -> Point(start.x, y) }
    else -> (start.x rangeTo end.x).toList().zip((start.y rangeTo end.y).toList()).map { (x, y) -> Point(x, y) }
}
fun Line.isHorizontal() = this.start.y == this.end.y
fun Line.isVertical() = this.start.x == this.end.x
private fun String.toLine(): Line {
    val (startText, endText) = this.split(" -> ")
    val (x1, y1) = startText.split(",")
    val (x2, y2) = endText.split(",")
    return Line(Point(x1.toInt(), y1.toInt()), Point(x2.toInt(), y2.toInt()))
}