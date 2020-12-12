package com.andb.adventofcode.year2020.day12

import com.andb.adventofcode.year2019.common.Coordinate
import com.andb.adventofcode.year2019.common.Direction
import java.io.File
import kotlin.math.absoluteValue

private val reader = File("src/com/andb/adventofcode/year2020/day12/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day12/test.txt").bufferedReader()

fun main(){
    //partOne()
    partTwo()
}

private fun partOne(){
    val instructions = reader.readLines().map { it.toInstruction() }
    println(instructions)
    println(instructions.executeMovement().let { it.x.absoluteValue + it.y.absoluteValue })
}

private fun partTwo(){
    val instructions = reader.readLines().map { it.toInstruction() }
    println(instructions.executeWaypoint().let { it.x.absoluteValue + it.y.absoluteValue })
}

private data class Instruction(val action: Action, val amount: Int)
enum class Action {
    North, South, East, West, Left, Right, Forward,
}

private fun String.toInstruction(): Instruction {
    val (action, amount) = this.partition { it !in '0'..'9' }
    return Instruction(action.toAction(), amount.toInt())
}

private fun String.toAction() = when (this) {
    "N" -> Action.North
    "S" -> Action.South
    "E" -> Action.East
    "W" -> Action.West
    "L" -> Action.Left
    "R" -> Action.Right
    "F" -> Action.Forward
    else -> throw Error("$this is not a valid action")
}

private fun List<Instruction>.executeMovement(): Coordinate {
    var coordinate = Coordinate(0, 0)
    var direction = Direction.EAST

    println("coordinate = $coordinate, direction = $direction")
    this.forEach {
        when (it.action) {
            Action.North -> coordinate.y += it.amount
            Action.South -> coordinate.y -= it.amount
            Action.East -> coordinate.x += it.amount
            Action.West -> coordinate.x -= it.amount
            Action.Left -> repeat(it.amount / 90) { direction = direction.ccw() }
            Action.Right -> repeat(it.amount / 90) { direction = direction.cw() }
            Action.Forward -> repeat(it.amount) { coordinate = coordinate.move(direction) }
        }
    }

    return coordinate
}

private fun List<Instruction>.executeWaypoint(): Coordinate {
    var relativeWaypoint = Coordinate(10, 1)
    var coordinate = Coordinate(0, 0)

    println("coordinate = $coordinate, relativeWaypoint = $relativeWaypoint")
    this.forEach {
        when (it.action) {
            Action.North -> relativeWaypoint.y += it.amount
            Action.South -> relativeWaypoint.y -= it.amount
            Action.East -> relativeWaypoint.x += it.amount
            Action.West -> relativeWaypoint.x -= it.amount
            Action.Left -> repeat(it.amount / 90) { relativeWaypoint = Coordinate(-relativeWaypoint.y, relativeWaypoint.x) }
            Action.Right -> repeat(it.amount / 90) { relativeWaypoint = Coordinate(relativeWaypoint.y, -relativeWaypoint.x) }
            Action.Forward -> repeat(it.amount) { coordinate += relativeWaypoint }
        }
    }

    return coordinate
}