package com.andb.adventofcode.year2019.day13

import com.andb.adventofcode.year2019.common.toIntcode
import com.andb.adventofcode.year2019.day8.splitEvery
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2019/day13/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day13/test.txt").bufferedReader()

fun main(){
    partOne()
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

}

private fun test(){

}

sealed class Tile(val x: Int, val y: Int, val type: Type){
    class Empty(x: Int, y: Int) : Tile(x, y, Type.EMPTY)
    class Wall(x: Int, y: Int) : Tile(x, y, Type.WALL)
    class Block(x: Int, y: Int) : Tile(x, y, Type.BLOCK)
    class Paddle(x: Int, y: Int) : Tile(x, y, Type.PADDLE)
    class Ball(x: Int, y: Int) : Tile(x, y, Type.BALL)

    enum class Type{
        EMPTY, WALL, BLOCK, PADDLE, BALL
    }
}

fun List<Int>.toTile(): Tile{
    val type = this[3]
    return when(type){
        1->Tile.Wall(this[0], this[1])
        2->Tile.Block(this[0], this[1])
        3->Tile.Paddle(this[0], this[1])
        4->Tile.Ball(this[0], this[1])
        else->Tile.Empty(this[0], this[1])
    }
}