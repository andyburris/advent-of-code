package com.andb.adventofcode.year2022.day13

import com.andb.adventofcode.year2020.common.mapAt
import com.andb.adventofcode.year2020.common.splitToGroups
import java.io.File
import java.lang.Error

private val reader = File("src/com/andb/adventofcode/year2022/day13/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day13/test.txt").bufferedReader()

fun main(){
    partTwo()
//    test()
}

private fun partOne(){
    val packets: List<Pair<List<ListValue>, List<ListValue>>> = reader.readLines().splitToGroups { it.isBlank() }.map { it[0].toPacket() to it[1].toPacket() }
    val inRightOrder = packets.map { it.inRightOrder() ?: throw Error("packet $it couldn't be determined") }
    println(inRightOrder)
    println(inRightOrder.foldIndexed(0) { index, acc, rightOrder -> acc + if(rightOrder) index + 1 else 0 })
}

private fun partTwo(){
    val packets: List<List<ListValue>> = reader.readLines().filter { it.isNotBlank() }.map { it.toPacket() }
    val dividerPackets = listOf("[[2]]", "[[6]]").map { it.toPacket() }
    assert(dividerPackets.all { it in packets })

    val sorted = packets.sortedWith { p1, p2 ->
        val inRightOrder = (p1 to p2).inRightOrder() ?: throw Error("packets ${p1 to p2} couldn't be determined")
        when(inRightOrder) {
            true -> -1
            false -> 1
        }
    }
    println(sorted)
    val indices = dividerPackets.map { sorted.indexOf(it) + 1 }
    println(indices[0] * indices[1])
}

private fun test(){
    val testEnclosures = "[1,[2,[3,4],5],6]".removePrefix("[").removeSuffix("]")
    println(testEnclosures)
    println(testEnclosures.separateEnclosures('[', ']').joinToString(" . "))
}

private sealed class ListValue {
    data class Integer(val value: Int) : ListValue()
    data class List(val values: kotlin.collections.List<ListValue>) : ListValue()
}

private fun Pair<List<ListValue>, List<ListValue>>.inRightOrder(): Boolean? {
    for ((left, right) in this.first.zip(this.second)) {
        when {
            left is ListValue.Integer && right is ListValue.Integer -> {
                if (left.value < right.value) return true
                if (left.value > right.value) return false
            }
            left is ListValue.List && right is ListValue.List -> {
                val inRightOrder = (left.values to right.values).inRightOrder()
                if (inRightOrder != null) return inRightOrder
                if (left.values.size < right.values.size) return true
                if (left.values.size > right.values.size) return false
            }
            else -> {
                val leftAsList = when (left) {
                    is ListValue.Integer -> listOf(left)
                    is ListValue.List -> left.values
                }
                val rightAsList = when(right) {
                    is ListValue.Integer -> listOf(right)
                    is ListValue.List -> right.values
                }
                val inRightOrder = (leftAsList to rightAsList).inRightOrder()
                if (inRightOrder != null) return inRightOrder
                if (leftAsList.size < rightAsList.size) return true
                if (leftAsList.size > rightAsList.size) return false
            }
        }
    }
    return when {
        this.first.size < this.second.size -> true
        this.first.size > this.second.size -> false
        else -> null
    }
}

private fun String.toPacket(): List<ListValue> {
    val enclosures = this.removePrefix("[").removeSuffix("]").separateEnclosures('[',']')
    println(enclosures.joinToString(" . "))
    return enclosures.flatMap { span ->
        when {
            span.first() == '[' -> listOf(ListValue.List(span.toPacket()))
            else -> span.split(",").filter { it.isNotBlank() }.map { ListValue.Integer(it.toInt()) }
        }
    }
}

private fun String.separateEnclosures(
    enclosureStart: Char,
    enclosureEnd: Char,
    dropEnclosures: Boolean = false
): List<String> = this.fold(listOf("") to 0) { (accTokens, numEnclosed), c ->
        val isEnclosureStart = (c == enclosureStart)
        val isEnclosureEnd = (c == enclosureEnd)

        when {
            isEnclosureStart && numEnclosed == 0 -> (accTokens + listOf(c.toString())) to 1
            isEnclosureEnd && numEnclosed == 1 -> (accTokens.mapAt(accTokens.size - 1) { it + c } + "") to 0
            else -> accTokens.mapAt(accTokens.size - 1) { it + c } to when {
                isEnclosureStart -> numEnclosed + 1
                isEnclosureEnd -> numEnclosed - 1
                else -> numEnclosed
            }
        }
    }.first.filter { it.isNotBlank() }
