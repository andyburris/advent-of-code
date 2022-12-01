package com.andb.adventofcode.year2022.day1

import com.andb.adventofcode.year2020.common.splitToGroups
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2022/day1/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day1/test.txt").bufferedReader()

fun main(){
    val inventories: List<List<Int>> = reader
        .readLines()
        .splitToGroups { it.isBlank() }.map{ l -> l.filter { it.isNotBlank() }.map { it.toInt() } }
    val sums: List<Int> = inventories.map { i -> i.sum() }
    partTwo(sums)
}

private fun partOne(sums: List<Int>){
    println(sums.max())
}

private fun partTwo(sums: List<Int>){
    val topThree = sums.sorted().takeLast(3)
    println(topThree.sum())
}

private fun test(){

}