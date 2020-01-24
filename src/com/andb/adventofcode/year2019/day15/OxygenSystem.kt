package com.andb.adventofcode.year2019.day15

import com.andb.adventofcode.year2019.common.Coordinate
import com.andb.adventofcode.year2019.common.Direction
import com.andb.adventofcode.year2019.common.toIntcode
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.io.File
import java.lang.Math.random
import javax.swing.JFrame
import javax.swing.JPanel

private val reader = File("src/com/andb/adventofcode/year2019/day15/input.txt").bufferedReader()
//private val testReader = File("src/com/andb/adventofcode/year2019/day15/test.txt").bufferedReader()
private const val WINDOW_SIZE = 800

fun main(){
    partOne()
}

private fun partOne(){
    val software = reader.toIntcode()
    val walls = mutableListOf<Coordinate>()
    val free = mutableListOf<Coordinate>()
    val robot = Robot(Coordinate(0, 0), Direction.NORTH)
    val camera = initDraw()
    software.onOutput = {output->
        when (output){
            0L->{
                walls.add(robot.move())
                //println("Hit wall, adding at ${robot.moveForward()}")
                robot.direction = if (random() > .5 )robot.direction.ccw() else robot.direction.cw()
                software.input = mutableListOf(robot.direction.backingInt.toLong())
                //println("new direction: ${software.input}")
            }
            1L->{
                robot.coordinate = robot.move()
                //println("Moved to free at ${robot.coordinate}")
                free.add(robot.coordinate)
                software.input = mutableListOf(robot.direction.backingInt.toLong())
            }
            2L-> {
                println("oxygen found at ${robot.coordinate}")
                calculateDistance(walls, free, robot.coordinate)
            }
            else->{
                throw (Exception("error - output = $output"))
            }
        }
        camera.free = free
        camera.walls = walls
        camera.current = robot.coordinate
        camera.repaint()
        Thread.sleep(2)
    }
    software.run()
}

private fun initDraw(): Camera{
    val frame = JFrame()
    val panel = Camera()
    frame.apply{
        add(panel)
        title = "Camera"
        size = Dimension(WINDOW_SIZE, WINDOW_SIZE)
        setLocationRelativeTo(null)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = true
    }
    return panel
}

private class Camera() : JPanel(){
    var walls = listOf<Coordinate>()
    var free = listOf<Coordinate>()
    var current = Coordinate(0, 0)
    override fun paint(g: Graphics) {
        val g2d = g as Graphics2D
        val amount = 50
        val increment = WINDOW_SIZE/(amount*2)
        for (i in -amount until amount){
            for (j in -amount until amount){
                g2d.color = when(Coordinate(j, i)){
                    current -> Color.RED
                    in walls -> Color.WHITE
                    in free -> Color.BLUE
                    else -> Color.BLACK
                }
                g2d.fillRect(i * increment + WINDOW_SIZE/2, j * increment + WINDOW_SIZE/2, increment, increment)
            }
        }
        g2d.color = Color.BLACK
    }
}


private fun calculateDistance(walls: List<Coordinate>, space: List<Coordinate>, oxygen: Coordinate){

}

private fun partTwo(){

}

private fun test(){

}

private data class Robot(var coordinate: Coordinate, var direction: Direction) {
    fun move(direction: Direction = this.direction): Coordinate {
        val newCoord = coordinate.copy()
        when(direction){
            Direction.NORTH -> newCoord.y++
            Direction.WEST -> newCoord.x--
            Direction.SOUTH -> newCoord.y--
            Direction.EAST -> newCoord.x++
        }
        return newCoord
    }
}