package com.andb.adventofcode.year2019.day3

import com.andb.adventofcode.year2019.common.Coordinate

class Path(components: List<PathComponent>) {
    val points = mutableListOf(Coordinate(0, 0))

    private fun currentX() = points.last().x
    private fun currentY() = points.last().y

    init {
        for (c in components) {
            for (i in 1..c.length) {
                when (c.direction) {
                    'L' -> points.add(Coordinate(currentX() - 1, currentY()))
                    'R' -> points.add(Coordinate(currentX() + 1, currentY()))
                    'U' -> points.add(Coordinate(currentX(), currentY() + 1))
                    'D' -> points.add(Coordinate(currentX(), currentY() - 1))
                }
            }
        }
        points.removeAt(0)
    }

    fun intersects(other: Path): List<Coordinate> = points.intersect(other.points).toList()
    fun intersectsTimed(other: Path): List<Pair<Int, Coordinate>> = intersects(other).map { Pair(points.indexOf(it) + other.points.indexOf(it) + 2, it) }
}

class PathComponent(val direction: Char, val length: Int)
fun String.toPathComponent() = PathComponent(this[0], substring(1).toInt())