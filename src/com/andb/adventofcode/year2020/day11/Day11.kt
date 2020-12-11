package com.andb.adventofcode.year2020.day11

import com.andb.adventofcode.year2020.common.adjacentChars
import com.andb.adventofcode.year2020.common.lineOfSightChars
import com.andb.adventofcode.year2020.common.mapCoordinated
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2020/day11/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day11/test.txt").bufferedReader()

fun main(){
    val seatLayout = reader.readText()
    partOne(seatLayout)
    partTwo(seatLayout)
}

private fun partOne(seatLayout: String){
    var last = seatLayout.lines()
    while (true) {
        val new = last.nextRound(lineOfSight = false)
        if (new == last) break
        last = new
    }
    println("occupied seats = ${last.sumBy { it.count { it == '#' } }}")
}

private fun partTwo(seatLayout: String){
    var last = seatLayout.lines()
    while (true) {
        val new = last.nextRound(lineOfSight = true)
        if (new == last) break
        last = new
    }
    println("occupied seats = ${last.sumBy { it.count { it == '#' } }}")
}

private fun List<String>.nextRound(lineOfSight: Boolean) = this.mapCoordinated { row, column, c ->
    val adjacentSeats = if (!lineOfSight) this.adjacentChars(row, column).count { it == '#' } else this.lineOfSightChars(row, column).count { line -> line.filter { it != '.' }.firstOrNull() == '#' }
    when (c) {
        'L' -> if (adjacentSeats == 0) '#' else 'L'
        '#' -> if (adjacentSeats >= if (!lineOfSight) 4 else 5) 'L' else '#'
        else -> c
    }
}