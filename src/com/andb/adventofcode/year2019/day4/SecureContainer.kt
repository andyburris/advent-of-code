package com.andb.adventofcode.year2019.day4

import com.andb.adventofcode.year2019.common.toDigits

private const val LOWER_BOUND = 130254
private const val UPPER_BOUND = 678275
private val allSixDigits = (0..999999).toList()


fun main() {
    println(partOne())
    println(partTwo())
}

fun partOne(): Int{
    return allSixDigits.filter { it in LOWER_BOUND..UPPER_BOUND }
        .map { it.toDigits() }
        .filter { it.hasDuplicates() }
        .filter { it == it.sorted()  }
        .size
}

fun partTwo(): Int{
    return allSixDigits.filter { it in LOWER_BOUND..UPPER_BOUND }
        .map { it.toDigits() }
        .filter { it.hasExactDuplicates() }
        .filter { it == it.sorted()  }
        .size
}

private fun <T> List<T>.hasDuplicates() = groupBy { it }.any { it.value.size > 1 }
private fun <T> List<T>.hasExactDuplicates() = groupBy { it }.any { it.value.size == 2 }