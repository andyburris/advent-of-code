package com.andb.adventofcode.year2020.common

fun <T> List<T>.pairCombinations(): List<Pair<T, T>> = sequence {
    this@pairCombinations.forEach { n1 ->
        this@pairCombinations.forEach { n2 ->
            yield(n1 to n2)
        }
    }
}.toList()

fun <T> List<T>.tripleCombinations(): List<Triple<T, T, T>> = sequence {
    this@tripleCombinations.forEach { n1 ->
        this@tripleCombinations.forEach { n2 ->
            this@tripleCombinations.forEach { n3 ->
                yield(Triple(n1, n2, n3))

            }
        }
    }
}.toList()