package com.andb.adventofcode.year2019.day1

import java.io.File

fun main(){
    println(partTwo())
}

private val reader = File("src/com/andb/adventofcode/year2019/day1/input.txt").bufferedReader()

private fun partOne(): Int{
    var sum = 0
    reader.forEachLine { mass-> sum += fuelFromMass(mass.toInt()) }
    return sum
}

private fun partTwo(): Int{
    var sum = 0
    reader.forEachLine { mass-> sum+= recursiveFuel(fuelFromMass(mass.toInt())) }
    return sum
}

private fun recursiveFuel(inital: Int): Int{
    return when{
        inital <= 0 -> 0
        else-> inital + recursiveFuel(fuelFromMass(inital))
    }
}

private fun fuelFromMass(mass: Int) = (mass/3) - 2