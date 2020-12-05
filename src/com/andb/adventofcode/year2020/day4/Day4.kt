package com.andb.adventofcode.year2020.day4

import com.andb.adventofcode.year2020.common.splitToGroups
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2020/day4/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day4/test.txt").bufferedReader()

fun main(){
    //partOne()
    partTwo()
}

private fun partOne(){
    val passports = reader.readLines().splitToGroups { it.isEmpty() }.map { it.joinToString(" ").toPassport() }
    println(passports.filter { it.hasValidFields() }.size)
}

private fun partTwo(){
    val passports = reader.readLines().splitToGroups { it.isEmpty() }.map { it.joinToString(" ").toPassport() }
    println(passports.filter { it.isValid() }.size)
}


private fun Passport.hasValidFields() =
    this.any { it.first == "byr" } &&
    this.any { it.first == "iyr" } &&
    this.any { it.first == "eyr" } &&
    this.any { it.first == "hgt" } &&
    this.any { it.first == "hcl" } &&
    this.any { it.first == "ecl" } &&
    this.any { it.first == "pid" }

private fun Passport.isValid() =
    this.any { it.first == "byr" && it.second.toInt() in 1920..2002 } &&
    this.any { it.first == "iyr" && it.second.toInt() in 2010..2020 } &&
    this.any { it.first == "eyr" && it.second.toInt() in 2020..2030 } &&
    this.any {
        it.first == "hgt" &&
        when (it.second.takeLast(2)) {
            "cm" -> it.second.dropLast(2).toInt() in 150..193
            "in" -> it.second.dropLast(2).toInt() in 59..76
            else -> false
        }
    } &&
    this.any { it.first == "hcl" && it.second.first() == '#' && it.second.drop(1).length == 6 && it.second.drop(1).all { c -> c in '0'..'9' || c in 'a'..'f' } } &&
    this.any { it.first == "ecl" && it.second in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth") } &&
    this.any { it.first == "pid" && it.second.length == 9 }


private fun String.toPassport(): Passport {
    return split(" ").map { it.toPassportField() }
}

private fun String.toPassportField(): Pair<String, String> {
    val indexOfSeparator = indexOf(":")
    val key = substring(0 until indexOfSeparator)
    val value = substring(indexOfSeparator + 1 until length)
    return Pair(key, value)
}

private typealias Passport = List<Pair<String, String>>