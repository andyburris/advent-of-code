package com.andb.adventofcode.year2020.day18

import java.io.File

private val reader = File("src/com/andb/adventofcode/year2020/day18/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day18/test.txt").bufferedReader()

fun main(){
    partOne()
}

private fun partOne(){
    val expressions = reader.readLines().map { it.toValues() }
    println(expressions.fold(0L) { acc, list ->  acc + list.evaluate() })
}

private fun partTwo(){

}

private fun test(){

}

private sealed class Value {
    sealed class Operator : Value() {
        object Plus : Operator()
        object Times : Operator()

        fun operate(left: Long, right: Long) = when(this) {
            Plus -> left + right
            Times -> left * right
        }
    }
    data class Number(val value: Long) : Value()
    data class Group(val values: List<Value> = emptyList(), val closed: Boolean = false) : Value()
}

//data class Operation(val left: Value, val operation: Value.Operator, val right: Value) : Value()


private fun String.toValues(): List<Value> {
    return this.filterNot { it == ' ' }.fold(emptyList()) { acc, c ->
        val nextValue = when {
            c.toString().toLongOrNull() != null -> Value.Number(c.toString().toLong())
            c == '+' -> Value.Operator.Plus
            c == '*' -> Value.Operator.Times
            c == '(' -> Value.Group()
            c == ')' -> return@fold acc.closeMostNested()
            else -> throw Error("Not supported ($c)")
        }
        acc.addToMostNested(nextValue)
    }
}

private fun List<Value>.addToMostNested(value: Value): List<Value> {
    val lastValue = this.lastOrNull()
    return if (lastValue is Value.Group && !lastValue.closed) this.dropLast(1) + lastValue.copy(values = lastValue.values.addToMostNested(value)) else this + value
}

private fun List<Value>.closeMostNested(): List<Value> {
    val lastValue = this.lastOrNull()
    if (lastValue !is Value.Group) throw Error("Can only close a group!")
    val nestedLast = lastValue.values.last()
    return if (nestedLast is Value.Group && !nestedLast.closed) this.dropLast(1) + lastValue.copy(values = lastValue.values.closeMostNested()) else this.dropLast(1) + lastValue.copy(closed = true)
}

private fun List<Value>.evaluate(): Long = this.fold<Value, Pair<Long, Value.Operator>>(Pair(0L, Value.Operator.Plus)) { acc, value ->
    when(value) {
        is Value.Operator -> acc.copy(second = value)
        is Value.Number -> acc.copy(first = acc.second.operate(acc.first, value.value))
        is Value.Group -> acc.copy(first = acc.second.operate(acc.first, value.values.evaluate()))
    }
}.first

