package com.andb.adventofcode.year2021.day4

import com.andb.adventofcode.columns
import com.andb.adventofcode.year2020.common.splitToGroups
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2021/day4/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2021/day4/test.txt").bufferedReader()

fun main() {
    val input = reader.readLines()
    val numbers = input.first().split(",").map { it.toInt() }
    val boards = input.drop(2).splitToGroups { it.isEmpty() }.map { it.toBoard() }
    partTwo(numbers, boards)
}

data class BingoAccumulation(val boards: List<Board>, val lastCalledNumber: Int)
private fun partOne(numbers: List<Int>, boards: List<Board>) {
    val boardsWithWinner = numbers
        .foldWhile(BingoAccumulation(boards, -1)) { (oldBoards, _), number ->
            val newBoards = oldBoards.map { it.mark(number) }
            BingoAccumulation(newBoards, number) to newBoards.none { it.isWon() }
        }
    val winningBoard = boardsWithWinner.boards.first { it.isWon() }
    val score = winningBoard.score(boardsWithWinner.lastCalledNumber)
    println(score)
}

private fun partTwo(numbers: List<Int>, boards: List<Board>) {
    val wonBoards = numbers
        .foldWhile(BingoAccumulation(boards, -1)) { (oldBoards, _), number ->
            val newBoards = oldBoards.map { it.mark(number) }
            val ordered = newBoards.partition { it.isWon() }.let { it.first + it.second }
            BingoAccumulation(ordered, number) to !newBoards.all { it.isWon() }
        }
    val lastWinningBoard = wonBoards.boards.last()
    val score = lastWinningBoard.score(wonBoards.lastCalledNumber)
    println(score)
}

private fun test() {

}

data class BoardSquare(val number: Int, val isMarked: Boolean)
typealias Board = List<List<BoardSquare>>

fun Board.isWon() =
    this.any { row -> row.all { it.isMarked } } || this.columns().any { col -> col.all { it.isMarked } }

fun Board.mark(number: Int): Board = this.map { row ->
    row.map { square ->
        when (square.number) {
            number -> square.copy(isMarked = true)
            else -> square
        }
    }
}
fun Board.score(lastCalledNumber: Int): Int = this.sumBy { row ->
    row.sumBy { if (it.isMarked) 0 else it.number }
} * lastCalledNumber

private fun List<String>.toBoard(): Board = this.map { row ->
    row.split(" ").filter { it.isNotEmpty() }.map { BoardSquare(it.toInt(), false) }
}

fun <T, R> List<T>.foldWhile(initial: R, operation: (acc: R, T) -> Pair<R, Boolean>): R {
    var accumulator = initial
    this.takeWhile { element ->
        val (newAccumulation, shouldContinue) = operation(accumulator, element)
        accumulator = newAccumulation
        shouldContinue
    }
    return accumulator
}