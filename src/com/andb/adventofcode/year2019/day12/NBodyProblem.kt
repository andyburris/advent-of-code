package com.andb.adventofcode.year2019.day12

import java.io.File

private val reader = File("src/com/andb/adventofcode/year2019/day12/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day12/test.txt").bufferedReader()

fun main(){
    partOne()
}

private fun partOne(){
    val moons: List<Moon> = reader.readLines().map { parseInputLine(it) }
    println(moons)

}

private fun partTwo(){

}

private fun test(){

}

private fun parseInputLine(input: String): Moon{
    val trimmed = input.filter { it in listOf(',', '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9') }
    val coords = trimmed.split(',').map { it.toInt() }
    return Moon(coords[0], coords[1], coords[2])
}

data class Moon(val x: Int, val y: Int, val z: Int){
    fun getGravity(others: List<Moon>): Velocity {
        return Velocity(others.sumBy { (it.x - this.x).polarize() })
    }

    private fun Int.polarize(): Int{
        return if (this == 0) this else if (this < 0) -1 else 1
    }
}

data class Velocity(val x: Int, val y: Int, val z: Int)