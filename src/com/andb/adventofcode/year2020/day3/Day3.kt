package com.andb.adventofcode.year2020.day3

import com.andb.adventofcode.year2020.common.takeEvery
import java.io.File
import kotlin.streams.toList

private val reader = File("src/com/andb/adventofcode/year2020/day3/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day3/test.txt").bufferedReader()

fun main(){
    //partOne()
    partTwo()
}

private fun partOne(){
    val lines = reader.lines().toList()
    println(lines.treeCount(3, 1))
}

private fun partTwo(){
    val lines = reader.lines().toList()
    println(lines.treeCount(1, 1) * lines.treeCount(3, 1) * lines.treeCount(5, 1) * lines.treeCount(7, 1) * lines.treeCount(1, 2))
}

private fun List<String>.treeCount(right: Int, down: Int) = takeEvery(down).foldIndexed(0) { index, acc, s -> if (s[(index * right) % this[0].length] == '#') acc + 1 else acc }.toLong()
