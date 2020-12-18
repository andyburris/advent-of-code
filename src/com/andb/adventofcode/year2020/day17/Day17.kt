package com.andb.adventofcode.year2020.day17

import com.andb.adventofcode.year2020.common.Quad
import com.andb.adventofcode.year2020.common.quadCombinations
import com.andb.adventofcode.year2020.common.tripleCombinations
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2020/day17/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day17/test.txt").bufferedReader()

fun main(){
    //partOne()
    partTwo(testReader.readLines().mapIndexed { row, line -> line.mapIndexed { col, c -> c.toCube4D(row, col) } }.flatten())
    partTwo(reader.readLines().mapIndexed { row, line -> line.mapIndexed { col, c -> c.toCube4D(row, col) } }.flatten())
}

private fun partOne(){
    val initialSlice = reader.readLines().mapIndexed { row, line -> line.mapIndexed { col, c -> c.toCube3D(row, col) } }.flatten()
    println(initialSlice)
    println(initialSlice.toStringPrint())
    println(initialSlice.cycle3D().toStringPrint())
    println(initialSlice.cycle3D().cycle3D().cycle3D().cycle3D().cycle3D().cycle3D().count { it.active })
}

private fun partTwo(initialSlice: List<Cube4D>){
    val count = (0 until 6).fold(initialSlice) { acc, i ->
        println("cycle $i start")
        val cycled = acc.cycle4D()
        println("cycle $i end")
        cycled
    }.count { it.active }
    println(count)
    //println(initialSlice.cycle4D().cycle4D().cycle4D().cycle4D().cycle4D().cycle4D().count { it.active })
}

private fun test(){

}

private data class Coordinate3D(val x: Int, val y: Int, val z: Int) {
    fun neighbors() = (-1..1).toList().tripleCombinations().map { Coordinate3D(x + it.first, y + it.second, z + it.third) } - this
}

private val neighborsOffset = (-1..1).toList().quadCombinations() - Quad(0, 0, 0, 0)
private data class Coordinate4D(val x: Int, val y: Int, val z: Int, val w: Int) {
    fun neighbors() = neighborsOffset.map { Coordinate4D(x + it.first, y + it.second, z + it.third, w + it.fourth) }
}
private fun Coordinate4D.toCoordinate3D() = Coordinate3D(x, y, z)

private data class Cube3D(val active: Boolean, val coordinate3D: Coordinate3D) {
    fun neighbors(cubes: List<Cube3D>) = coordinate3D.neighbors().map { coord -> cubes.find { it.coordinate3D == coord } ?: Cube3D(false, coord) }
}
private data class Cube4D(val active: Boolean, val coordinate4D: Coordinate4D) {
    //fun neighbors(cubes: List<Cube4D>) = coordinate4D.neighbors().map { coord -> cubes.find { it.coordinate4D == coord } ?: Cube4D(false, coord) }
    fun neighbors(cubes: List<Cube4D>): List<Cube4D> {
        val neighbors = coordinate4D.neighbors().toMutableList()
        val exist = cubes.filter {
            val inNeighbors = it.coordinate4D in neighbors
            if (inNeighbors) neighbors.remove(it.coordinate4D)
            inNeighbors
        }
        val notExist = neighbors.map { Cube4D(false, it) }
        return exist + notExist
    }
}

private fun List<Cube3D>.toStringPrint() = this
    .groupBy { it.coordinate3D.z }
    .mapValues { (key, value) ->
        val minX = value.minBy { it.coordinate3D.x }!!.coordinate3D.x
        value
            .sortedBy { it.coordinate3D.x }
            .sortedBy { it.coordinate3D.y }
            .joinToString("") { (if (it.coordinate3D.x == minX) "\n" else "") + (if (it.active) '#' else '.') }
    }
    .toList()
    .joinToString("\n") { "z=${it.first}" + it.second }
private fun List<Cube4D>.toStringPrint4D() = this
    .groupBy { it.coordinate4D.w }
    .mapValues { (key, value) ->
        value.map { Cube3D(it.active, it.coordinate4D.toCoordinate3D()) }.toStringPrint()
    }
    .toList()
    .joinToString("\n") { "w=${it.first}" + it.second }

private fun List<Cube3D>.cube3DNeighbors() = this.flatMap { it.neighbors(this) }.distinct()
private fun List<Cube4D>.cube4DNeighbors() = this.flatMap { it.neighbors(this) }.distinct()
private fun Char.toCube3D(row: Int, column: Int) = when(this) {
    '#' -> Cube3D(true, Coordinate3D(column, row, 0))
    '.' -> Cube3D(false, Coordinate3D(column, row, 0))
    else -> throw Error("Must be one of # or . (currently $this)")
}
private fun Char.toCube4D(row: Int, column: Int) = when(this) {
    '#' -> Cube4D(true, Coordinate4D(column, row, 0, 0))
    '.' -> Cube4D(false, Coordinate4D(column, row, 0, 0))
    else -> throw Error("Must be one of # or . (currently $this)")
}


private fun List<Cube3D>.cycle3D() = this.cube3DNeighbors().map {
    val activeNeighbors = it.neighbors(this).count { it.active }
    val active = when {
        it.active && (activeNeighbors == 2 || activeNeighbors == 3) -> true
        !it.active && activeNeighbors == 3 -> true
        else -> false
    }
    return@map it.copy(active = active)
}

private fun List<Cube4D>.cycle4D(neighbors: List<Cube4D> = this.cube4DNeighbors()) = neighbors.map {
    val activeNeighbors = it.neighbors(this).count { it.active }
    val active = when {
        it.active && (activeNeighbors == 2 || activeNeighbors == 3) -> true
        !it.active && activeNeighbors == 3 -> true
        else -> false
    }
    return@map it.copy(active = active)
}