package com.andb.adventofcode.year2020.day18

import java.io.File

private val reader = File("src/com/andb/adventofcode/year2020/day18/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day18/test.txt").bufferedReader()

fun main() {
    val expressions = reader.readLines().map { it.toValues() }
    partOne(expressions)
    partTwo(expressions)
}

private fun partOne(expressions: List<List<Value>>) {
    println(expressions.fold(0L) { acc, list -> acc + list.evaluateBasic() })
}

private fun partTwo(expressions: List<List<Value>>) {
    println(expressions.fold(0L) { acc, list -> acc + list.evaluateAdvanced() })
}

private sealed class Value {
    sealed class Operator : Value() {
        object Plus : Operator()
        object Times : Operator()

        fun operate(left: Long, right: Long) = when (this) {
            Plus -> left + right
            Times -> left * right
        }
    }

    data class Number(val value: Long) : Value()
    data class Group(val values: List<Value> = emptyList(), val closed: Boolean = false) : Value()
}

private fun String.toValues(): List<Value> {
    return this.filterNot { it == ' ' }.fold(emptyList()) { acc, c ->
        val nextValue = when (c) {
            in '0'..'9' -> Value.Number(c.toString().toLong())
            '+' -> Value.Operator.Plus
            '*' -> Value.Operator.Times
            '(' -> Value.Group()
            ')' -> return@fold acc.closeMostNested()
            else -> throw Error("Not supported ($c)")
        }
        acc.addToMostNested(nextValue)
    }
}

private fun List<Value>.addToMostNested(value: Value): List<Value> {
    val lastValue = this.lastOrNull()
    return if (lastValue is Value.Group && !lastValue.closed) this.dropLast(1) + lastValue.copy(
        values = lastValue.values.addToMostNested(
            value
        )
    ) else this + value
}

private fun List<Value>.closeMostNested(): List<Value> {
    val lastValue = this.lastOrNull()
    if (lastValue !is Value.Group) throw Error("Can only close a group!")
    val nestedLast = lastValue.values.last()
    return if (nestedLast is Value.Group && !nestedLast.closed) this.dropLast(1) + lastValue.copy(values = lastValue.values.closeMostNested()) else this.dropLast(
        1
    ) + lastValue.copy(closed = true)
}

private fun List<Value>.evaluateBasic(): Long =
    this.fold<Value, Pair<Long, Value.Operator>>(Pair(0L, Value.Operator.Plus)) { acc, value ->
        when (value) {
            is Value.Operator -> acc.copy(second = value)
            is Value.Number -> acc.copy(first = acc.second.operate(acc.first, value.value))
            is Value.Group -> acc.copy(first = acc.second.operate(acc.first, value.values.evaluateBasic()))
        }
    }.first

private fun List<Value>.evaluateAdvanced(): Long = this.groupByPriority().evaluateBasic()

private fun List<Value>.groupByPriority(): List<Value> {
    val groupNested = this.map { if (it is Value.Group) it.copy(values = it.values.groupByPriority()) else it }
    val newGrouped = mutableListOf(groupNested.first())
    groupNested
        .drop(1)
        .chunked(2)
        .map { (operator, value) ->
            if (operator is Value.Operator.Plus) {
                val left = newGrouped.removeAt(newGrouped.size - 1)
                val group = listOf(left) + operator + value
                newGrouped.add(Value.Group(group, true))
            } else {
                newGrouped.add(operator)
                newGrouped.add(value)
            }
        }
    return newGrouped
}