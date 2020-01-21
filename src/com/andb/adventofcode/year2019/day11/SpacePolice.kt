package com.andb.adventofcode.year2019.day11

import com.andb.adventofcode.year2019.common.Coordinate
import com.andb.adventofcode.year2019.common.toIntcode
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2019/day11/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day11/test.txt").bufferedReader()

fun main() {
    partTwo()
}

private fun partOne() {
    val paintedWhite = mutableListOf<Coordinate>()
    val painted = mutableListOf<Coordinate>()
    val software = reader.readLine().split(",").map { it.toLong() }.toIntcode()
    val robot = Robot(Coordinate(0, 0), Direction.NORTH)
    software.input = mutableListOf(0)
    var isPaintOutput = true
    software.onOutput = { output ->
        println("output = $output, isPaintOutput = $isPaintOutput, robot = $robot")
        if (isPaintOutput && !painted.contains(robot.coordinate)){
            painted.add(robot.coordinate.copy())
            println("painting ${if (output == 0L) "black" else "white"} at ${robot.coordinate}")
        }
        when {
            isPaintOutput && output == 0L -> paintedWhite.removeIf { it == robot.coordinate }
            isPaintOutput && output == 1L -> paintedWhite.add(robot.coordinate.copy())
            !isPaintOutput -> {
                robot.changeDirection(output)
                robot.moveForward()
                println("robot changed to $robot after output of $output")
                val input = if (paintedWhite.contains(robot.coordinate)) 1L else 0L
                println("inputting $input to computer")
                software.input = mutableListOf(input)
            }
        }
        isPaintOutput = !isPaintOutput
    }
    software.run()
    println(painted)
    println(painted.size)
}

private fun partTwo() {
    val paintedWhite = mutableListOf<Coordinate>()
    val painted = mutableListOf<Coordinate>()
    val software = reader.readLine().split(",").map { it.toLong() }.toIntcode()
    val robot = Robot(Coordinate(0, 0), Direction.NORTH)
    software.input = mutableListOf(1)
    var isPaintOutput = true
    software.onOutput = { output ->
        println("output = $output, isPaintOutput = $isPaintOutput, robot = $robot")
        if (isPaintOutput && !painted.contains(robot.coordinate)){
            painted.add(robot.coordinate.copy())
            println("painting ${if (output == 0L) "black" else "white"} at ${robot.coordinate}")
        }
        when {
            isPaintOutput && output == 0L -> paintedWhite.removeIf { it == robot.coordinate }
            isPaintOutput && output == 1L -> paintedWhite.add(robot.coordinate.copy())
            !isPaintOutput -> {
                robot.changeDirection(output)
                robot.moveForward()
                println("robot changed to $robot after output of $output")
                val input = if (paintedWhite.contains(robot.coordinate)) 1L else 0L
                println("inputting $input to computer")
                software.input = mutableListOf(input)
            }
        }
        isPaintOutput = !isPaintOutput
    }
    software.run()
    val minX = painted.minBy { it.x }!!.x
    val maxX = painted.maxBy { it.x }!!.x
    val minY = painted.minBy { it.y }!!.y
    val maxY = painted.maxBy { it.y }!!.y

    for (y in maxY downTo minY){
        for (x in minX..maxX){
            if(paintedWhite.contains(Coordinate(x, y))){
                print("█")
            }else{
                print(" ")
            }
        }
        println()
    }

}

private fun test() {

}

private data class Robot(var coordinate: Coordinate, var direction: Direction) {
    fun changeDirection(output: Long) {
        direction = if (output == 0L) direction.ccw() else direction.cw()
    }
    fun moveForward(){
        when(direction){
            Direction.NORTH -> coordinate.y++
            Direction.WEST -> coordinate.x--
            Direction.SOUTH -> coordinate.y--
            Direction.EAST -> coordinate.x++
        }
    }
}

private enum class Direction {
    NORTH, WEST, SOUTH, EAST;

    fun ccw() = when (this) {
        NORTH -> WEST
        WEST -> SOUTH
        SOUTH -> EAST
        EAST -> NORTH
    }

    fun cw() = this.ccw().ccw().ccw()
}