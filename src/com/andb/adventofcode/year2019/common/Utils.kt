package com.andb.adventofcode.year2019.common

fun Int.toDigits() : List<Int> = this.toString().toCharArray().map { it.toString().toInt() }

fun List<Int>.extendTo(to: Int, insert: Int = 0): List<Int>{
    val mutable = this.toMutableList()
    for (i in size until to){
        mutable.add(insert)
    }
    return mutable
}