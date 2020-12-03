package com.andb.adventofcode.year2020.day2

import java.io.File
import kotlin.streams.toList

private val reader = File("src/com/andb/adventofcode/year2020/day2/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2020/day2/test.txt").bufferedReader()

fun main(){
    //partOne()
    partTwo()
}


private fun partOne(){
    val passwords = reader.lines().map { it.toPasswordPolicy() }.toList()
    println(passwords.count { policy -> policy.password.filter { it == policy.char }.length in policy.startRange..policy.endRange })
}

private fun partTwo(){
    val passwords = reader.lines().map { it.toPasswordPolicy() }.toList()
    println(passwords.count { policy -> (policy.password[policy.startRange - 1] == policy.char) xor (policy.password[policy.endRange - 1] == policy.char) })
}

private data class PasswordPolicy(val char: Char, val startRange: Int, val endRange: Int, val password: String)

private fun String.toPasswordPolicy(): PasswordPolicy {
    val separatorIndex = this.indexOf(":")
    val password = this.substring(separatorIndex + 2 until this.length)
    val char = this[separatorIndex - 1]
    val dashIndex = this.indexOf("-")
    val startRange = this.substring(0 until dashIndex).toInt()
    val endRange = this.substring(dashIndex + 1 until separatorIndex - 2).toInt()

    return PasswordPolicy(char, startRange, endRange, password)
}