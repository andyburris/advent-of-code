package com.andb.adventofcode.year2020.day20

import com.andb.adventofcode.year2020.common.column
import com.andb.adventofcode.year2020.common.row
import com.andb.adventofcode.year2020.common.splitToGroups
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2020/day20/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day20/test.txt").bufferedReader()

fun main(){
    partOne()
}

private fun partOne(){
    val tiles = reader.readLines().splitToGroups { it.isEmpty() }.map { it.toTile() }
    tiles.toGrid()
}

private fun partTwo(){

}

private fun test(){

}

data class Borders(val top: String, val bottom: String, val left: String, val right: String)
data class Tile(val id: Int, val array: List<String>) {
    fun borders() = Borders(array.row(0), array.row(array.size - 1), array.column(0), array.column(array.size - 1))
}
private fun List<String>.toTile() = Tile(this.first().removePrefix("Tile ").removeSuffix(":").toInt(), this.drop(1))

private fun List<Tile>.toGrid(): List<List<Tile>> {
    println(this.first().borders())
    val allBorders: List<Borders> = this.map { it.borders() }
    val corners = this.filter {
        val itBorders: Borders = it.borders()
        val otherBorders: List<Borders> = allBorders.filter { it != itBorders }
        itBorders.intersect(otherBorders) == 2
    }
    println(corners)
    println(corners.map { it.id })
    println(corners.count())
    println(corners.fold(1L) { acc, tile -> acc * tile.id })
    return emptyList()
}

fun Borders.intersectTile(allOtherBorders: List<Tile>): Int {
    val intersects = allOtherBorders.filter { it.borders().intersect(this) }
    println(intersects)
    return intersects.count()
}

fun Borders.intersect(allOtherBorders: List<Borders>): Int {
    val intersects = allOtherBorders.filter { it.intersect(this) }
    println(intersects)
    return intersects.count()
}

fun Borders.intersect(other: Borders): Boolean {
    val allFlipsAndRotations = listOf(other.top, other.top.reversed(), other.bottom, other.bottom.reversed(), other.left, other.left.reversed(), other.right, other.right.reversed())
    return this.top in allFlipsAndRotations || this.left in allFlipsAndRotations || this.right in allFlipsAndRotations || this.bottom in allFlipsAndRotations
    //return this.intersectFixedRotation(other) || this.intersectFixedRotation(other.cw()) || this.intersectFixedRotation(other.ccw()) || this.intersectFixedRotation(other.turn180()) || this.intersectFixedRotation(other.flipHorizontal()) || this.intersectFixedRotation(other.flipVertical())
}
fun Borders.cw() = Borders(this.left.reversed(), this.right.reversed(), this.bottom, this.top)
fun Borders.ccw() = Borders(this.right, this.left, this.top.reversed(), this.bottom.reversed())
fun Borders.turn180() = Borders(this.bottom.reversed(), this.top.reversed(), this.right.reversed(), this.left.reversed())
fun Borders.flipVertical() = Borders(this.bottom, this.top, this.left.reversed(), this.right.reversed())
fun Borders.flipHorizontal() = Borders(this.top.reversed(), this.bottom.reversed(), this.right, this.left)
fun Borders.intersectFixedRotation(other: Borders) = this.top == other.bottom || this.left == other.right || this.right == other.left || this.bottom == other.top