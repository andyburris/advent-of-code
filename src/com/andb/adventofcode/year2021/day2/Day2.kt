package com.andb.adventofcode.year2021.day2

import java.io.File

private val reader = File("src/com/andb/adventofcode/year2021/day2/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2021/day2/test.txt").bufferedReader()

fun main() {
    partTwo()
}

data class Command(val direction: Direction, val amount: Int)
enum class Direction {
    Forward, Down, Up
}
data class SubmarinePositionOne(val depth: Int = 0, val horizontal: Int = 0)
data class SubmarinePositionTwo(val depth: Int = 0, val horizontal: Int = 0, val aim: Int = 0)

private fun partOne() {
    val commands = reader.readLines().map { it.toCommand() }
    val endPosition = commands.fold(SubmarinePositionOne()) { acc, command ->
        when(command.direction) {
            Direction.Forward -> acc.copy(horizontal = acc.horizontal + command.amount)
            Direction.Down -> acc.copy(depth = acc.depth + command.amount)
            Direction.Up -> acc.copy(depth = acc.depth - command.amount)
        }
    }
    println(endPosition.depth * endPosition.horizontal)
}

private fun partTwo() {
    val commands = reader.readLines().map { it.toCommand() }
    val endPosition = commands.fold(SubmarinePositionTwo()) { acc, command ->
        when(command.direction) {
            Direction.Forward -> acc.copy(
                horizontal = acc.horizontal + command.amount,
                depth = acc.depth + command.amount * acc.aim
            )
            Direction.Down -> acc.copy(aim = acc.aim + command.amount)
            Direction.Up -> acc.copy(aim = acc.aim - command.amount)
        }
    }
    println(endPosition.depth * endPosition.horizontal)
}

private fun test() {

}

private fun String.toCommand(): Command {
    val (directionString, distanceString) = this.split(" ")
    val direction = directionString.toDirection()
    val distance = distanceString.toInt()
    return Command(direction, distance)
}

private fun String.toDirection() = when(this){
    "forward" -> Direction.Forward
    "down" -> Direction.Down
    "up" -> Direction.Up
    else -> throw Error("Invalid direction string: $this")
}