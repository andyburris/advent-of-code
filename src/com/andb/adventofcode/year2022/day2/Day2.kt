package com.andb.adventofcode.year2022.day2

import java.io.File
import java.lang.Error

private val reader = File("src/com/andb/adventofcode/year2022/day2/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day2/test.txt").bufferedReader()

fun main(){
    partTwo()
}

private fun partOne(){
    val strategies = reader.readLines().map { it.split(' ') }.map { it[0].toRockPaperScissorsResult() to it[1].toRockPaperScissorsResult() }
    println(strategies.sumOf { it.second.scoreVersus(it.first) })
}

private fun partTwo(){
    val strategies = reader.readLines().map { it.split(' ') }.map { it[0].toRockPaperScissorsResult() to it[1].toRoundResult() }
    println(strategies.sumOf {
        val toThrow = it.second.forOpponent(it.first)
            toThrow.scoreVersus(it.first)
        }
    )
}

private fun test(){

}

private enum class RockPaperScissorsResult {
    Rock, Paper, Scissors;

    fun beats(other: RockPaperScissorsResult): Boolean = when(this) {
        Rock -> other == Scissors
        Paper -> other == Rock
        Scissors -> other == Paper
    }

    fun beatsVal(): RockPaperScissorsResult = when(this) {
        Rock -> Scissors
        Paper -> Rock
        Scissors -> Paper
    }

    fun ties(other: RockPaperScissorsResult): Boolean = when(this) {
        Rock -> other == Rock
        Paper -> other == Paper
        Scissors -> other == Scissors
    }

    fun tiesVal() = when(this) {
        Rock -> Rock
        Paper -> Paper
        Scissors -> Scissors
    }

    fun loses(other: RockPaperScissorsResult): Boolean = when(this) {
        Rock -> other == Paper
        Paper -> other == Scissors
        Scissors -> other == Rock
    }

    fun losesVal() = when(this) {
        Rock -> Paper
        Paper -> Scissors
        Scissors -> Rock
    }
}

private enum class RoundResult {
    Win, Tie, Loss;

    fun forOpponent(opponentThrow: RockPaperScissorsResult): RockPaperScissorsResult = when(this) {
        Win -> opponentThrow.losesVal()
        Tie -> opponentThrow.tiesVal()
        Loss -> opponentThrow.beatsVal()
    }
}

private fun String.toRockPaperScissorsResult() = when(this) {
    "A", "X" -> RockPaperScissorsResult.Rock
    "B", "Y" -> RockPaperScissorsResult.Paper
    "C", "Z" -> RockPaperScissorsResult.Scissors
    else -> throw Error("not valid")
}

private fun String.toRoundResult() = when(this) {
    "X" -> RoundResult.Loss
    "Y" -> RoundResult.Tie
    "Z" -> RoundResult.Win
    else -> throw Error("not valid")
}

private fun RockPaperScissorsResult.scoreVersus(other: RockPaperScissorsResult): Int {
    val shapeScore = when(this) {
        RockPaperScissorsResult.Rock -> 1
        RockPaperScissorsResult.Paper -> 2
        RockPaperScissorsResult.Scissors -> 3
    }
    val resultScore = when {
        this.beats(other) -> 6
        this.ties(other) -> 3
        else -> 0
    }
    return shapeScore + resultScore
}