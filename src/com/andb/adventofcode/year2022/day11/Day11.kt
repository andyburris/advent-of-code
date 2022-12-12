package com.andb.adventofcode.year2022.day11

import com.andb.adventofcode.year2020.common.splitToGroups
import java.io.File
import java.lang.Error

private val reader = File("src/com/andb/adventofcode/year2022/day11/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day11/test.txt").bufferedReader()

fun main(){
    //partOne()
    partTwo()
}

private fun partOne(){
    val monkeys = reader.readLines().splitToGroups { it.isBlank() }.map { it.toMonkey() }
    println(monkeys)
    val numRounds = 20
    val afterAllRounds = (0 until numRounds).fold(monkeys) { roundMonkeys, roundIndex ->
        val finalMonkeys = roundMonkeys.indices.fold(roundMonkeys) { accMonkeys, currentMonkeyIndex ->
            val currentMonkey = accMonkeys[currentMonkeyIndex]
            val allThrown = currentMonkey.items.fold(accMonkeys) { accMonkeys, item ->
                val inspectionWorryLevel = currentMonkey.operation(item)
                val reliefWorryLevel = inspectionWorryLevel / 3
                val throwTo = if (currentMonkey.test.testFunction(reliefWorryLevel)) currentMonkey.test.throwToIfTrue else currentMonkey.test.throwToIfFalse
                accMonkeys.mapIndexed { index, monkey -> if (index == throwTo) monkey.copy(items = monkey.items + reliefWorryLevel) else monkey }
            }
            val updatedInspections = allThrown.mapIndexed { index, monkey -> if (index == currentMonkeyIndex) monkey.copy(items = emptyList(), totalInspections = monkey.totalInspections + currentMonkey.items.size.toInt()) else monkey }
            updatedInspections
        }
        finalMonkeys
    }
    println(afterAllRounds.sortedBy { it.totalInspections }.takeLast(2).let { it[0].totalInspections * it[1].totalInspections })
}

private fun partTwo(){
    val monkeys = reader.readLines().splitToGroups { it.isBlank() }.map { it.toMonkey() }
    val maxModulo = monkeys.fold(1) { acc, monkey -> acc * monkey.test.divisibleBy }.toInt()
    println(monkeys)
    val numRounds = 10000L
    val afterAllRounds = (0 until numRounds).fold(monkeys) { roundMonkeys, roundIndex ->
        val finalMonkeys = roundMonkeys.indices.fold(roundMonkeys) { accMonkeys, currentMonkeyIndex ->
            val currentMonkey = accMonkeys[currentMonkeyIndex]
            val allThrown = currentMonkey.items.fold(accMonkeys) { accMonkeys, item ->
                val inspectionWorryLevel = currentMonkey.operation(item)
                val moddedWorryLevel = inspectionWorryLevel % maxModulo
                val throwTo = if (currentMonkey.test.testFunction(moddedWorryLevel)) currentMonkey.test.throwToIfTrue else currentMonkey.test.throwToIfFalse
                accMonkeys.mapIndexed { index, monkey -> if (index == throwTo) monkey.copy(items = monkey.items + moddedWorryLevel) else monkey }
            }
            val updatedInspections = allThrown.mapIndexed { index, monkey -> if (index == currentMonkeyIndex) monkey.copy(items = emptyList(), totalInspections = monkey.totalInspections + currentMonkey.items.size.toInt()) else monkey }
            updatedInspections
        }
        finalMonkeys
    }
    println(afterAllRounds.sortedBy { it.totalInspections }.takeLast(2).let { it[0].totalInspections.toLong() * it[1].totalInspections })
}

private fun test(){

}

private data class Monkey(
    val items: List<Long>,
    val operation: (Long) -> Long,
    val test: Test,
    val totalInspections: Int = 0
) {
    data class Test(
        val divisibleBy: Int,
        val throwToIfTrue: Int,
        val throwToIfFalse: Int,
    ) {
        val testFunction: (Long) -> Boolean = { it % divisibleBy == 0L }
    }
}

private fun List<String>.toMonkey(): Monkey {
    val rawStartingItems: String = this[1].trim()
    val rawOperation: String = this[2].trim()
    val rawTest: List<String> = this.drop(3)

    val startingItems = rawStartingItems.removePrefix("Starting items: ").split(", ").map { it.toLong() }
    val operation: (Long) -> Long = rawOperation.removePrefix("Operation: new = ").split(" ").let {
        val term1 = it[0].toTerm()
        val term2 = it[2].toTerm()

        when(it[1].toOperator()) {
            Operator.Times -> { old -> (if (term1 is Term.Number) term1.value else old) * (if (term2 is Term.Number) term2.value else old) }
            Operator.Plus -> { old -> (if (term1 is Term.Number) term1.value else old) + (if (term2 is Term.Number) term2.value else old) }
        }
    }

    val test = rawTest.toMonkeyTest()
    return Monkey(startingItems, operation, test)
}

private sealed class Term {
    object Old : Term()
    data class Number(val value: Long) : Term()
}

private fun String.toTerm() = when {
    this == "old" -> Term.Old
    this.toIntOrNull() != null -> Term.Number(this.toLong())
    else -> throw Error("$this is an invalid Term")
}

private enum class Operator {
    Times, Plus
}

private fun String.toOperator() = when(this) {
    "*" -> Operator.Times
    "+" -> Operator.Plus
    else -> throw Error("$this is an invalid Operator")
}

private fun List<String>.toMonkeyTest(): Monkey.Test {
    val divisibleBy = this[0].trim().removePrefix("Test: divisible by ").toInt()
    val ifTrue = this[1].trim().removePrefix("If true: throw to monkey ").toInt()
    val ifFalse = this[2].trim().removePrefix("If false: throw to monkey ").toInt()
    return Monkey.Test(divisibleBy, ifTrue, ifFalse)
}
