package com.andb.adventofcode.year2021.common

fun List<Int>.median(): Int = this.map { it.toLong() }.median().toInt()

fun List<Long>.median(): Long {
    val sorted = this.sorted()
    val middleIndex = sorted.size / 2
    return when {
        sorted.size.isEven() -> (sorted[middleIndex - 1] + sorted[middleIndex]) / 2
        else -> sorted[middleIndex]
    }
}

private fun Int.isEven() = this % 2 == 0
