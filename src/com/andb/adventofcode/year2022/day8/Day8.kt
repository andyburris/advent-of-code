package com.andb.adventofcode.year2022.day8

import com.andb.adventofcode.columns
import com.andb.adventofcode.year2020.common.Quad
import com.andb.adventofcode.year2020.common.pairCombinations
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2022/day8/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day8/test.txt").bufferedReader()

fun main() {
    val trees: List<List<Int>> = reader.readLines().map { line -> line.toList().map{ it.digitToInt() } }
    //partOne(trees)
    partTwo(trees)
}

private fun partOne(trees: List<List<Int>>) {

    val allCoordinates = (trees.indices).flatMap { row -> trees[0].indices.map { col -> row to col }}
    println(allCoordinates)

    val asColumns = trees.columns()

    val visibleTrees = allCoordinates.filter { (row, col) ->
        val isVisible = trees[row].take(col + 1).isVisible() //left
                || asColumns[col].take(row + 1).isVisible() //top
                || trees[row].drop(col).reversed().isVisible() //right
                || asColumns[col].drop(row).reversed().isVisible() //bottom
        isVisible
    }
    println(visibleTrees.size)
}

private fun partTwo(trees: List<List<Int>>) {
    val scenicScores: List<List<Int>> = trees.sightlines().map { row ->
        row.map { it.score() }
    }
    println(scenicScores.flatten().maxOf { it })
}

private fun test() {

}

private fun List<Int>.isVisible(): Boolean = this.last() > (this.dropLast(1).maxOfOrNull { it } ?: -1)

private fun List<List<Int>>.sightlines(): List<List<Sightlines>> {
    val asColumns = this.columns()
    return this.mapIndexed { row, treeRow ->
        treeRow.mapIndexed { col, treeHeight ->
            Sightlines(
                left = this[row].take(col + 1),
                top = asColumns[col].take(row + 1),
                right = this[row].drop(col).reversed(),
                bottom = asColumns[col].drop(row).reversed(),
            )
        }
    }
}

private data class Sightlines(
    val top: List<Int>,
    val left: List<Int>,
    val bottom: List<Int>,
    val right: List<Int>,
)

private fun Sightlines.score() = listOf(top, left, bottom, right).fold(1) { acc, line -> acc * line.amountVisible() }
private fun List<Int>.amountVisible() = (this.dropLast(1).takeLastWhile { it < this.last() }.size + 1).coerceAtMost(this.size - 1)