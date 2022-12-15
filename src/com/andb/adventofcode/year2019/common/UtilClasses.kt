package com.andb.adventofcode.year2019.common

import kotlin.math.absoluteValue

data class Coordinate(var x: Int, var y: Int) {
    fun move(direction: Direction): Coordinate {
        val newCoord = copy()
        when (direction) {
            Direction.NORTH -> newCoord.y++
            Direction.WEST -> newCoord.x--
            Direction.SOUTH -> newCoord.y--
            Direction.EAST -> newCoord.x++
        }
        return newCoord
    }

    fun surrounding(): List<Coordinate> {
        return listOf(copy(x = x + 1), copy(x = x - 1), copy(y = y + 1), copy(y = y - 1))
    }

    operator fun plusAssign(coordinate: Coordinate) {
        this.x += coordinate.x
        this.y += coordinate.y
    }

    fun manhattanDistanceTo(other: Coordinate): Int = (this.x - other.x).absoluteValue + (this.y - other.y).absoluteValue
}

enum class Direction(val backingInt: Int) {
    NORTH(1), WEST(3), SOUTH(2), EAST(4);

    fun ccw() = when (this) {
        NORTH -> WEST
        WEST -> SOUTH
        SOUTH -> EAST
        EAST -> NORTH
    }

    fun cw() = this.ccw().ccw().ccw()

    fun inverse() = when (this) {
        NORTH -> SOUTH
        SOUTH -> NORTH
        WEST -> EAST
        EAST -> WEST
    }

    companion object {
        fun from(backingInt: Int) = when (backingInt) {
            1 -> NORTH
            2 -> SOUTH
            3 -> WEST
            4 -> EAST
            else -> NORTH
        }
    }
}

class DefaultHashMap<K, V>(private val default: V) : HashMap<K, V>() {
    override fun get(key: K): V {
        ensureKey(key)
        return super.get(key)!!
    }

    override fun put(key: K, value: V): V {
        ensureKey(key)
        return super.put(key, value)!!
    }

    private fun ensureKey(key: K) {
        if (!this.containsKey(key)) {
            super.put(key, default)
        }
    }
}