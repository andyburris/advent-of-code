package com.andb.adventofcode.year2019.day13

import com.andb.adventofcode.year2019.common.toIntcode
import com.andb.adventofcode.year2019.day8.splitEvery
import kotlinx.coroutines.delay
import java.awt.*
import java.io.File
import javax.swing.JFrame
import javax.swing.JPanel

private val reader = File("src/com/andb/adventofcode/year2019/day13/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day13/test.txt").bufferedReader()

class ArcadeCabinet() : JPanel(){

}

fun main(){
    partTwo()
}

private fun partOne(){
    val software = reader.readLine().split(",").map { it.toLong() }.toIntcode()
    println(software)
    software.run()
    val output = software.allOutputs
    println(output)
    val tiles = output.map { it.toInt() }.splitEvery(3).map { it.toTile() }
    println(tiles.filter { it is Tile.Block }.size)
}

private fun partTwo(){
    val software = reader.readLine().split(",").map { it.toLong() }.toIntcode()
    software.program[0] = 2
    software.input = mutableListOf(0)
    val tiles = mutableListOf<Tile>()
    val panel = initDraw()
    var paddleX = -1
    var ballX = -1
    var score = 0
    software.onOutput = {
        if (software.allOutputs.size %3 ==0){
            if (software.allOutputs[0]==-1L){
                score = software.allOutputs[2].toInt()
                panel.score = score
            }
            val tile = software.allOutputs.map { it.toInt() }.toTile()
            software.allOutputs.clear()
            println("adding tile: $tile")
            tiles.removeIf { it.x == tile.x && it.y == tile.y }
            tiles.add(tile)
            panel.tiles = tiles
            panel.score
            panel.repaint()
            if (tile.type == Tile.Type.BALL){
                ballX = tile.x
            }
            if(tile.type == Tile.Type.PADDLE){
                paddleX = tile.x
            }
            val direction = if(paddleX == ballX) 0 else if (paddleX > ballX) -1 else 1
            software.input = mutableListOf(direction.toLong())

        }
    }

    software.run()

}

private fun initDraw(): Screen{
    val frame = JFrame()
    val panel = Screen()
    frame.apply{
        add(panel)
        title = "Arcade Screen"
        size = Dimension(800, 800)
        setLocationRelativeTo(null)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = true
    }
    return panel
}

private class Screen() : JPanel(){
    var tiles = listOf<Tile>()
    var score = 0

    override fun paint(g: Graphics) {
        val g2d = g as Graphics2D
        val amount = 50
        val increment = 800/amount
        for (i in 0 until amount){
            for (j in 0 until amount){
                val tile = tiles.toList().find { it.x == i && it.y == j }
                g2d.color = when(tile?.type){
                    Tile.Type.EMPTY->Color.BLACK
                    Tile.Type.WALL->Color.WHITE
                    Tile.Type.BLOCK->Color.BLUE
                    Tile.Type.PADDLE->Color.RED
                    Tile.Type.BALL->Color.GREEN
                    else -> Color.BLACK
                }
                g2d.fillRect(i * increment, j * increment, increment, increment)
            }
        }
        g2d.color = Color.BLACK
        g2d.drawString(score.toString(), 10, 10)
    }
}

private fun test(){

}

sealed class Tile(val x: Int, val y: Int, val type: Type){
    class Empty(x: Int, y: Int) : Tile(x, y, Type.EMPTY)
    class Wall(x: Int, y: Int) : Tile(x, y, Type.WALL)
    class Block(x: Int, y: Int) : Tile(x, y, Type.BLOCK)
    class Paddle(x: Int, y: Int) : Tile(x, y, Type.PADDLE)
    class Ball(x: Int, y: Int) : Tile(x, y, Type.BALL)

    override fun toString(): String = "Tile(x: $x, y: $y, type: $type)"

    enum class Type{
        EMPTY, WALL, BLOCK, PADDLE, BALL
    }
}

fun List<Int>.toTile(): Tile{
    val type = this[2]
    return when(type){
        1->Tile.Wall(this[0], this[1])
        2->Tile.Block(this[0], this[1])
        3->Tile.Paddle(this[0], this[1])
        4->Tile.Ball(this[0], this[1])
        else->Tile.Empty(this[0], this[1])
    }
}