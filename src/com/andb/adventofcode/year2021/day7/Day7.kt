package com.andb.adventofcode.year2021.day7

import com.andb.adventofcode.year2021.common.median
import java.io.File
import kotlin.math.absoluteValue

private val reader = File("src/com/andb/adventofcode/year2021/day7/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2021/day7/test.txt").bufferedReader()

fun main(){
    partTwo()
}

private fun partOne(){
    val horizontalPositions = reader.readLine().split(",").map { it.toInt() }
    val leastFuelPosition = horizontalPositions.leastFuelPositionOne()
    val fuelUsed = horizontalPositions.fuelToOne(leastFuelPosition)
    println("fuel used = $fuelUsed")
}

private fun partTwo(){
    val horizontalPositions = reader.readLine().split(",").map { it.toInt() }
    val leastFuelPosition = horizontalPositions.leastFuelPositionTwo()
    val fuelUsed = horizontalPositions.fuelToTwo(leastFuelPosition)
    println("fuel used = $fuelUsed")
}

private fun test(){
    println(listOf(1).median())
    println(listOf(1, 3).median())
    println(listOf(1, 3, 18).median())
}

private fun List<Int>.fuelToOne(position: Int): Int = this.sumBy { (it - position).absoluteValue }
private fun List<Int>.fuelToTwo(position: Int): Int = this.sumBy { (it - position).absoluteValue.triangleNumber() }

private fun List<Int>.leastFuelPositionOne() = this.median()
private fun List<Int>.leastFuelPositionTwo(): Int = (this.min()!!..this.max()!!).minBy { position ->
    this.fuelToTwo(position)
}!!

private fun Int.triangleNumber() = (this downTo 0).sum()