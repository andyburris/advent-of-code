package com.andb.adventofcode.year2020.day5

import java.io.File

private val reader = File("src/com/andb/adventofcode/year2020/day5/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day5/test.txt").bufferedReader()

fun main(){
    partTwo()
}

private fun partOne(){
    val passes = reader.readLines().map { it.toBoardingPass() }
    println(passes.maxBy { it.seatID }?.seatID)
}

private fun partTwo(){
    val passes = reader.readLines().map { it.toBoardingPass() }
    val frontRow = passes.minBy { it.row }!!.row
    val backRow = passes.maxBy { it.row }!!.row
    val middleRows = passes.filter { it.row != frontRow && it.row != backRow }
    val rowWithMissing =
        middleRows.groupBy { it.row }.values.first { row -> !row.map { it.column }.containsAll((0 until 8).toList()) }
    val missingColumn = ((0 until 8).toList() - rowWithMissing.map { it.column }).first()
    val mySeat = BoardingPass(rowWithMissing.first().row, missingColumn)
    println(mySeat)
}

private data class BoardingPass (val row: Int, val column: Int, val seatID: Int = (8 * row) + column)

private fun String.toBoardingPass(): BoardingPass {
    val rowFinder = substring(0 until 7)
    val columnFinder = substring(7 until length)

    val row = rowFinder.fold(0 until 128) { acc, c -> if (c == 'F') acc.lowerHalf() else acc.upperHalf() }.last
    val column = columnFinder.fold(0 until 8) { acc, c -> if (c == 'L') acc.lowerHalf() else acc.upperHalf() }.last
    return BoardingPass(row, column)
}

private fun IntRange.lowerHalf() = start..(start + (endInclusive-start)/2)
private fun IntRange.upperHalf() = (start + (endInclusive-start)/2)..endInclusive