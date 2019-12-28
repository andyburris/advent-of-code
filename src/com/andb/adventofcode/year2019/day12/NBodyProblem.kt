package com.andb.adventofcode.year2019.day12

import com.andb.adventofcode.year2019.common.pow
import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.max

private val reader = File("src/com/andb/adventofcode/year2019/day12/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day12/test.txt").bufferedReader()

fun main() {
    partTwo()
    println(783615256%4702)
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
    val moons: List<Moon> = testReader.readLines().map { parseInputLine(it) }
    val initialState = moons.map { it.copy() }
    val initialX = initialState.map { it.x }
    val initialXVel = initialState.map { it.velocity.x }
    val initialY = initialState.map { it.y }
    val initialYVel = initialState.map { it.velocity.y }
    val initialZ = initialState.map { it.z }
    val initialZVel = initialState.map { it.velocity.z }
    println("After 0 steps: ")
    println(moons.joinToString(separator = "\n"))

    var step = 1L
    var xCycle = -1L
    var yCycle = -1L
    var zCycle = -1L
    while(xCycle < 0 || yCycle < 0 || zCycle < 0){
        moons.forEach { it.velocity = it.getGravity(moons) }
        moons.forEach { it.applyVelocity() }
        if (moons.map { it.x } == initialX && moons.map { it.velocity.x } == initialXVel){ xCycle = step }
        if (moons.map { it.y } == initialY && moons.map { it.velocity.y } == initialYVel){ yCycle = step }
        if (moons.map { it.z } == initialZ && moons.map { it.velocity.z } == initialZVel){ zCycle = step }
        step++
    }

    println("xCycle: $xCycle, yCycle: $yCycle, zCycle: $zCycle")
    val lcm = lcm(lcm(xCycle, yCycle), zCycle)
    println(lcm)
}

private fun test() {
    val v1 = 35
    val v2 = 27
    val lcm = lcm(v1.toLong(), v2.toLong())
    println(lcm)
    check(lcm%v1 == 0L)
    check(lcm%v2 == 0L)

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

    private fun Int.polarize(): Int = if (this == 0) this else if (this < 0) -1 else 1

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

fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b