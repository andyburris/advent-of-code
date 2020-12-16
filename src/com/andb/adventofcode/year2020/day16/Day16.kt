package com.andb.adventofcode.year2020.day16

import com.andb.adventofcode.year2020.common.splitToGroups
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2020/day16/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day16/test.txt").bufferedReader()

fun main(){
    //partOne()
    partTwo()
    test()
}

private fun partOne(){
    val (rulesStrings, yourTicket, nearbyTickets) = reader.readLines().splitToGroups { it.isEmpty() }
    println(rulesStrings)
    val rules = rulesStrings.map { it.split(": ")[1].split(" or ").map { range -> range.split("-").let { it[0].toInt()..it[1].toInt() } } }
    println(rules)
    val tickets = nearbyTickets.drop(1).map { line -> line.split(",").map { it.toInt() } }
    println(tickets)
    val errorRate = tickets.map { ticket -> ticket.filter { num -> !rules.any { num in it } }.sum() }
    println(errorRate)
    println(errorRate.sum())
}

private fun partTwo(){
    val (rulesStrings, yourTicketString, nearbyTickets) = reader.readLines().splitToGroups { it.isEmpty() }
    val rules = rulesStrings.map { it.toRule() }
    val yourTicket = yourTicketString.last().toTicket()
    val tickets = nearbyTickets.drop(1).map { line -> line.toTicket() }

    val validTickets = tickets.filter { ticket -> ticket.none { num -> !rules.any { num in it.ranges } } }
    println(validTickets)
    val orderedRules = (rules.indices).map { index ->
        val values = validTickets.map { it[index] }
        println(values)
        val validRules = rules.filter { it.validFor(values) }
        validRules
    }
    println(orderedRules)
    val usedRules = mutableListOf<Rule>()
    val indexedRules = orderedRules.withIndex().sortedBy { it.value.size }.map { (index, validRules) ->
        val rule = (validRules - usedRules).first()
        usedRules.add(rule)
        IndexedValue(index, rule)
    }.sortedBy { it.index }
    println(indexedRules)

    println(indexedRules.filter { it.value.name.startsWith("departure") }.map { yourTicket[it.index] }.fold(1L) { acc, i -> acc * i })
}

private fun test(){
    println(3 in listOf(0..1, 4..19))
}

private data class Rule(val name: String, val ranges: List<IntRange>) {
    fun validFor(values: List<Int>) = values.all { it in ranges }
}
private fun String.toRule(): Rule {
    val (name, rangeString) = this.split(": ")
    val ranges = rangeString.split(" or ").map { range -> range.split("-").let { it[0].toInt()..it[1].toInt() } }
    return Rule(name, ranges)
}

private operator fun List<IntRange>.contains(value: Int) = this.any { value in it }
private fun String.toTicket() = this.split(",").map { it.toInt() }