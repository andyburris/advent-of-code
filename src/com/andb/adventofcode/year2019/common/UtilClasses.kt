package com.andb.adventofcode.year2019.common

data class Coordinate(var x: Int, var y: Int)

enum class Direction(val backingInt: Int) {
    NORTH(1), WEST(2), SOUTH(3), EAST(4);

    fun ccw() = when (this) {
        NORTH -> WEST
        WEST -> SOUTH
        SOUTH -> EAST
        EAST -> NORTH
    }

    fun cw() = this.ccw().ccw().ccw()
}

class DefaultHashMap<K, V>(private val default: V) : HashMap<K, V>(){
    override fun get(key: K): V {
        ensureKey(key)
        return super.get(key)!!
    }

    override fun put(key: K, value: V): V {
        ensureKey(key)
        return super.put(key, value)!!
    }

    private fun ensureKey(key: K){
        if(!this.containsKey(key)){
            super.put(key, default)
        }
    }
}