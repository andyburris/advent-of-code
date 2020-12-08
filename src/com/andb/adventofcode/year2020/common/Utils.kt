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

fun <T> List<T>.takeEvery(amount: Int) = filterIndexed { index, _ -> index % amount == 0 }

fun <T> List<T>.splitToGroups(separator: (T) -> Boolean): MutableList<MutableList<T>> {
    val outList: MutableList<MutableList<T>> = mutableListOf(mutableListOf())
    this.forEach {
        if (separator.invoke(it)) {
            outList.add(mutableListOf())
        } else {
            outList.last().add(it)
        }
    }
    return outList
}

fun <T> List<T>.mapAt(index: Int, transform: (old: T) -> T) = this.toMutableList().also { it[index] = transform(it[index]) }.toList()