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

fun <T> List<T>.quadCombinations(): List<Quad<T, T, T, T>> = sequence {
    this@quadCombinations.forEach { n1 ->
        this@quadCombinations.forEach { n2 ->
            this@quadCombinations.forEach { n3 ->
                this@quadCombinations.forEach { n4 ->
                    yield(Quad(n1, n2, n3, n4))
                }
            }
        }
    }
}.toList()

public data class Quad<out A, out B, out C, out D>(
    public val first: A,
    public val second: B,
    public val third: C,
    public val fourth: D
) : java.io.Serializable {

    /**
     * Returns string representation of the [Quad] including its [first], [second] and [third] values.
     */
    public override fun toString(): String = "($first, $second, $third, $fourth)"
}

/**
 * Converts this triple into a list.
 * @sample samples.misc.Tuples.tripleToList
 */
public fun <T> kotlin.Triple<T, T, T>.toList(): List<T> = listOf(first, second, third)


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

fun List<Long>.lcm() = this.fold(1L) { acc, l -> com.andb.adventofcode.lcm(acc, l) }