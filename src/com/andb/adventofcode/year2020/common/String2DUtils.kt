package com.andb.adventofcode.year2020.common

fun String.forEachCoordinated(action: (row: Int, column: Int, c: Char) -> Unit) =
    this.lines().forEachCoordinated(action)

fun List<String>.forEachCoordinated(action: (row: Int, column: Int, c: Char) -> Unit) =
    this.forEachIndexed { row, line ->
        line.forEachIndexed { col, c ->
            action.invoke(row, col, c)
        }
    }

fun <R> String.mapCoordinated(action: (row: Int, column: Int, c: Char) -> R) = this.lines().mapCoordinated(action)
fun <R> List<String>.mapCoordinated(action: (row: Int, column: Int, c: Char) -> R) = this.mapIndexed { row, line ->
    line.mapIndexed { col, c ->
        action.invoke(row, col, c)
    }
}.map { it.joinToString("") }

fun List<String>.getCoordinate(row: Int, column: Int) = if (row !in this.indices || column !in this[0].indices) null else this[row][column]

data class Pattern(val horizontalStep: Int, val verticalStep: Int) {
    companion object {
        val TopLeft = Pattern(-1, -1)
        val Top = Pattern(0, -1)
        val TopRight = Pattern(1, -1)
        val Left = Pattern(-1, 0)
        val Right = Pattern(1, 0)
        val BottomLeft = Pattern(-1, 1)
        val Bottom = Pattern(0, 1)
        val BottomRight = Pattern(1, 1)
    }
}

fun List<String>.getAdjacent(row: Int, column: Int, adjacentPattern: Pattern) = this.getCoordinate(row + adjacentPattern.horizontalStep, column + adjacentPattern.verticalStep)

fun List<String>.adjacentChars(
    row: Int,
    column: Int,
    adjacentPatterns: List<Pattern> = listOf(Pattern.TopLeft, Pattern.Top, Pattern.TopRight, Pattern.Left, Pattern.Right, Pattern.BottomLeft, Pattern.Bottom, Pattern.BottomRight)
): List<Char?> {
    return adjacentPatterns.map { this.getAdjacent(row, column, it) }
}

fun List<String>.lineOfSightChars(
    row: Int,
    column: Int,
    adjacentPatterns: List<Pattern> = listOf(Pattern.TopLeft, Pattern.Top, Pattern.TopRight, Pattern.Left, Pattern.Right, Pattern.BottomLeft, Pattern.Bottom, Pattern.BottomRight)
): List<List<Char?>> {
    return adjacentPatterns
        .map { pattern ->
            val rowLength = when {
                pattern.verticalStep < 0 -> row
                pattern.verticalStep == 0 -> 0
                else  -> size - row
            }
            val columnLength = when {
                pattern.horizontalStep < 0 -> column
                pattern.horizontalStep == 0 -> 0
                else  -> this[0].length - column
            }

            val lineOfSightLength = maxOf(rowLength, columnLength)
            return@map List(lineOfSightLength) { i ->
                Pair(
                    row + (pattern.verticalStep * (i + 1)),
                    column + (pattern.horizontalStep * (i + 1))
                )
            }
        }.map { line ->
            line.map { this.getCoordinate(it.first, it.second) }
        }
}

fun List<String>.column(index: Int): String = this.map { it[index] }.joinToString("")
fun List<String>.row(index: Int): String = this[index]
fun List<String>.cw(): List<String> = this.mapIndexed { index: Int, s: String -> this.column(size - index - 1) }
fun List<String>.ccw(): List<String> = this.mapIndexed { index: Int, s: String -> this.column(index) }

typealias String2D = List<String>