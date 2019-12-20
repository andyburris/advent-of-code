package com.andb.adventofcode.year2019.day12

import java.io.File
import kotlin.math.absoluteValue

private val reader = File("src/com/andb/adventofcode/year2019/day12/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day12/test.txt").bufferedReader()

fun main() {
    partOne()
}

private fun partOne() {
    val moons: List<Moon> = reader.readLines().map { parseInputLine(it) }
    println("After 0 steps: ")
    println(moons.joinToString(separator = "\n"))

    for (i in 1..1000) {
        moons.forEach { it.velocity = it.getGravity(moons) }
        moons.forEach { it.applyVelocity() }
        println("\nAfter $i ${if (i == 1) "step" else "steps"}:")
        println(moons.joinToString(separator = "\n"))
    }

    val energy = moons.sumBy { it.energy }
    println(energy)

}

private fun partTwo() {

}

private fun test() {

}

private fun parseInputLine(input: String): Moon {
    val trimmed = input.filter { it in listOf(',', '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9') }
    val coords = trimmed.split(',').map { it.toInt() }
    return Moon(coords[0], coords[1], coords[2], Velocity())
}

data class Moon(var x: Int, var y: Int, var z: Int, var velocity: Velocity) {
    val energy: Int
        get() = (x.absoluteValue + y.absoluteValue + z.absoluteValue) * (velocity.x.absoluteValue + velocity.y.absoluteValue + velocity.z.absoluteValue)

    fun getGravity(others: List<Moon>): Velocity {
        val gravity = Velocity(
            others.sumBy { (it.x - this.x).polarize() },
            others.sumBy { (it.y - this.y).polarize() },
            others.sumBy { (it.z - this.z).polarize() }
        )
        return gravity + velocity
    }

    fun applyVelocity() {
        this.x += velocity.x
        this.y += velocity.y
        this.z += velocity.z
    }

    private fun Int.polarize(): Int {
        return if (this == 0) this else if (this < 0) -1 else 1
    }

    override fun toString(): String {
        return "pos=<x: $x, y: $y, z: $z>, vel=$velocity"
    }
}

data class Velocity(val x: Int = 0, val y: Int = 0, val z: Int = 0) {
    operator fun plus(other: Velocity) = Velocity(x + other.x, y + other.y, z + other.z)
    override fun toString(): String {
        return "<x: $x, y: $y, z: $z>"
    }
}