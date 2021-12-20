package com.andb.adventofcode

data class Point(val x: Int, val y: Int)

fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b

fun <T> List<List<T>>.columns(): List<List<T>> {
    if (this.isEmpty()) emptyList<T>()
    val emptyColumns = this.first().map { mutableListOf<T>() }
    this.forEach { row ->
        row.forEachIndexed { index, t ->
            emptyColumns[index].add(t)
        }
    }
    return emptyColumns.map { it.toList() }
}

infix fun Int.rangeTo(other: Int) = when {
    this <= other -> this..other
    else -> this downTo other
}