package com.andb.adventofcode.year2022.day12

import com.andb.adventofcode.year2019.common.Coordinate
import java.io.File
import java.lang.Error

private val reader = File("src/com/andb/adventofcode/year2022/day12/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day12/test.txt").bufferedReader()

fun main(){
    partOne()
    partTwo()
}

private fun partOne(){
    val rawElevations = testReader.readLines().map { it.toList() }
    val startingCoordinate = Coordinate(rawElevations.indexOfFirst { 'S' in it }, rawElevations.first { 'S' in it }.indexOf('S'))
    val endingCoordinate = Coordinate(rawElevations.indexOfFirst { 'E' in it }, rawElevations.first { 'E' in it }.indexOf('E'))

    val replacedElevations = rawElevations.replaceCoordinate(startingCoordinate) { 'a' }.replaceCoordinate(endingCoordinate) { 'z' }
    println(replacedElevations.joinToString("\n"){ it.joinToString("") })
    val edges: List<TopographyEdge> = replacedElevations.flatMapIndexed { y, rawElevationRow ->
        rawElevationRow.flatMapIndexed { x, rawElevation ->
            val thisCoord = Coordinate(x, y)
            val surroundingCoords = thisCoord.surrounding()
            val edges = surroundingCoords.filter { rawElevation.canMoveTo(rawElevations[y][x]) }.map { TopographyEdge(thisCoord, it) }
            edges
        }
    }
    println(edges)
    val path = breadthFirstSearch(edges, startingCoordinate, endingCoordinate)
    println(path.size)
}

private fun breadthFirstSearch(edges: List<TopographyEdge>, startingCoordinate: Coordinate, endingCoordinate: Coordinate): List<Coordinate> {
    val queue = mutableListOf(TopographyEdge(null, startingCoordinate))
    val visitedPaths = mutableListOf<Pair<Coordinate, List<Coordinate>>>()

    while (queue.isNotEmpty()) {
        val currentEdge = queue.removeAt(0)
        val current = currentEdge.to
        if (current !in visitedPaths.map { it.first }) {
            visitedPaths += current to (visitedPaths.find { it.first == currentEdge.from }?.second ?: emptyList()) + current
            if (current == endingCoordinate) return visitedPaths.first { it.first == endingCoordinate }.second
            queue += edges.filter { it.from == current }
        }
    }
    throw Error("not found")
}

private fun partTwo(){

}

private fun test(){

}

private fun Char.canMoveTo(other: Char) = this >= other + 1

private data class TopographyGraph(val nodes: List<TopographyNode>, val edges: List<TopographyEdge>)
private data class TopographyNode(val elevation: Char, val coordinate: Coordinate)
private data class TopographyEdge(val from: Coordinate?, val to: Coordinate, val length: Int = 1)

private fun <T> List<List<T>>.replaceCoordinate(coordinate: Coordinate, transform: (T) -> T) = this.mapIndexed { index, ts -> if (index == coordinate.y) ts.mapIndexed { index, t -> if (index == coordinate.x) transform(t) else t } else ts }