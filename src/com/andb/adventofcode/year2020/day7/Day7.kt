package com.andb.adventofcode.year2020.day7

import java.io.File

private val reader = File("src/com/andb/adventofcode/year2020/day7/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day7/test.txt").bufferedReader()

fun main(){
    partTwo()
}

private fun partOne(){
    val rules = reader.readLines().map { it.toRuleSet() }
    println(rules)
    println(rules.countCanEventuallyContain("shiny gold"))
}

private fun partTwo(){
    val rules = reader.readLines().map { it.toRuleSet() }
    val shinyGoldBagRule = rules.first { it.bagType == "shiny gold" }
    println(shinyGoldBagRule)
    println(shinyGoldBagRule.nestedBags(rules))
}

private fun test(){

}

data class RuleSet(val bagType: String, val contents: List<Pair<String, Int>>)
private fun String.toRuleSet(): RuleSet {
    val (bagType, contentsString) = this.split(" bags contain ")
    val contents = when (val toParse = contentsString.dropLast(1)) {
        "no other bags" -> emptyList()
        else -> toParse.split(", ").map {
            val (number, bag) = it.split(" ", limit = 2)
            println("num = $number, bag = $bag")
            Pair(bag.replace(" bags", "").replace(" bag", ""), number.toInt())
        }
    }
    return RuleSet(bagType, contents)
}

private fun List<RuleSet>.countCanEventuallyContain(bagType: String): Int {
    return this.count { ruleSet ->
        ruleSet.canEventuallyContain(bagType, this)
    }
}

private fun RuleSet.canEventuallyContain(bagType: String, rules: List<RuleSet>): Boolean {
    return contents.any { it.first == bagType } || contents.any { contentPair -> rules.first { it.bagType == contentPair.first }.canEventuallyContain(bagType, rules) }
}

private fun RuleSet.nestedBags(rules: List<RuleSet>): Int {
    val inThis = contents.map { it.second }.sum()
    val inNested = contents.map { contentPair -> rules.first { it.bagType == contentPair.first }.nestedBags(rules) * contentPair.second }.sum()
    return inThis + inNested
}