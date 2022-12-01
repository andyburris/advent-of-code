package com.andb.adventofcode.year2021.day10

import com.andb.adventofcode.year2021.common.median
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2021/day10/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2021/day10/test.txt").bufferedReader()

fun main() {
    partTwo()
}

private fun partOne() {
    val answer = reader
        .readLines()
        .mapNotNull { it.readChunks().illegalCharacters.firstOrNull() }
        .map { illegalCharacterScore.getValue(it) }
        .sum()
    println(answer)
}

private fun partTwo() {
    val answer = reader
        .readLines()
        .map { it.readChunks() }
        .filter { it.illegalCharacters.isEmpty() }
        .map { it.stack.closingCharactersScore() }
        .also { println("lines = $it") }
        .median()
    println(answer)
}

private fun List<Char>.closingCharactersScore() = this
    .map { it.closingCharacter() }
    .map { closingCharacterScore.getValue(it) }
    .foldRight(0L) { s, acc -> (acc * 5) + s }

private fun test() {

}

val illegalCharacterScore = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137,
)

val closingCharacterScore = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4,
)

private data class SyntaxReader(val stack: List<Char> = emptyList(), val illegalCharacters: List<Char> = emptyList())

private fun SyntaxReader.stackCharacter(c: Char) = this.copy(stack = stack + c)
private fun SyntaxReader.dropCharacter() = this.copy(stack = stack.dropLast(1))
private fun SyntaxReader.withIllegalCharacter(c: Char) = this.copy(illegalCharacters = illegalCharacters + c)
private fun String.readChunks(): SyntaxReader = this.fold(SyntaxReader()) { reader, c ->
    when (c) {
        '(', '[', '{', '<' -> reader.stackCharacter(c)
        ')', ']', '}', '>' -> when {
            c.openingCharacter() != reader.stack.last() -> reader.withIllegalCharacter(c).dropCharacter()
            else -> reader.dropCharacter()
        }
        else -> throw Error("Not a valid character")
    }
}

private fun Char.openingCharacter() = when (this) {
    ')' -> '('
    ']' -> '['
    '}' -> '{'
    '>' -> '<'
    else -> throw Error("$this is not a closing character")
}

private fun Char.closingCharacter() = when (this) {
    '(' -> ')'
    '[' -> ']'
    '{' -> '}'
    '<' -> '>'
    else -> throw Error("$this is not an opening character")
}