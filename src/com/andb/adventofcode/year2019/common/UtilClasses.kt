package com.andb.adventofcode.year2019.common

data class Coordinate(var x: Int, var y: Int)

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