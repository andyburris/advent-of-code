package com.andb.adventofcode.year2020.day6

import com.andb.adventofcode.year2020.common.splitToGroups
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2020/day6/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day6/test.txt").bufferedReader()

fun main(){
    //partOne()
    partTwo()
}

private fun partOne(){
    val groups = reader.readLines().splitToGroups { it.isEmpty() }.map { it.joinToString(" ").toGroupAnswers() }
    println(groups.sumBy { it.size })
}

private fun partTwo(){
    val groups = reader.readLines().splitToGroups { it.isEmpty() }.map { it.joinToString("\n").toAllGroupAnswers() }
    println(groups.sumBy { it.size })
}

private fun String.toGroupAnswers() = this.groupBy { it }.filter { it.key in 'a'..'z' }.keys.toList()
private fun String.toAllGroupAnswers() = this.groupBy { it }.filter { it.key in 'a'..'z' && it.value.size == this.lines().size }.keys.toList()