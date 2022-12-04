package com.andb.adventofcode.year2022.day3

import com.andb.adventofcode.year2019.day8.splitEvery
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2022/day3/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day3/test.txt").bufferedReader()

fun main(){
    partTwo()
}

private fun partOne(){
    val rucksackContents: List<Pair<List<Char>, List<Char>>> = reader.readLines()
        .map{ line -> line.halves() }
        .map{ rucksack -> rucksack.first.toList() to rucksack.second.toList() }
    val rucksackShared: List<Char> = rucksackContents
        .map { contents -> contents.first.intersect(contents.second).first() }
    val priorities = rucksackShared.map { c -> c.priority() }
    println(rucksackShared)
    println(priorities)
    println(priorities.sum())
}

private fun partTwo(){
    val rucksackContents: List<List<Char>> = reader.readLines()
        .map{ line -> line.toList() }
    val rucksackGroups = rucksackContents.splitEvery(3)
    val groupShared = rucksackGroups.map { group -> group[0].intersect(group[1]).intersect(group[2]).first() }
    val priorities = groupShared.map { c -> c.priority() }
    println(priorities.sum())
}

private fun test(){

}

private fun String.halves(): Pair<String, String> {
    val halfLength = this.length / 2
    return this.take(halfLength) to this.drop(halfLength)
}

private fun Char.priority() = when(this) {
    in 'a'..'z' ->  ('a'..'z').indexOf(this) + 1
    in 'A'..'Z' ->  ('A'..'Z').indexOf(this) + 27
    else -> throw Error("invalid char")
}