package com.andb.adventofcode.year2021.day3

import java.io.File

private val reader = File("src/com/andb/adventofcode/year2021/day3/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2021/day3/test.txt").bufferedReader()

fun main(){
    partTwo()
}

private fun partOne(){
    val binaryInput = reader.readLines()
    println(binaryInput.gammaNumber() * binaryInput.epsilonNumber())
}


private fun List<String>.gammaNumber() = this.frequencies().map { it.moreFrequent() }.joinToString("").toInt(2)
private fun List<String>.epsilonNumber() = this.frequencies().map { it.lessFrequent() }.joinToString("").toInt(2)

private data class BitFrequency(val zeroes: Int = 0, val ones: Int = 0)
private fun BitFrequency.moreFrequent(): Int = when {
    this.zeroes > this.ones -> 0
    else -> 1
}
private fun BitFrequency.lessFrequent(): Int = when {
    this.ones < this.zeroes -> 1
    else -> 0
}
private fun List<String>.frequencies(): List<BitFrequency> {
    val bitsLength = this.first().length
    return this.fold((0 until bitsLength).map { BitFrequency() }) { acc, bits ->
        acc.zip(bits.toList()).map { (frequency, bit) ->
            when(bit) {
                '0' -> frequency.copy(zeroes = frequency.zeroes + 1)
                '1' -> frequency.copy(ones = frequency.ones + 1)
                else -> throw Error("'$bit' must be either '0' or '1'")
            }
        }
    }
}

private fun partTwo(){
    val binaryInput = reader.readLines()
    val oxygenGeneratorRating = binaryInput.oxygenGeneratorRating()
    val scrubberRating = binaryInput.scrubberRating()
    println("oxygenGeneratorRating = $oxygenGeneratorRating")
    println("scrubberRating = $scrubberRating")
    println(oxygenGeneratorRating * scrubberRating)
}

private fun List<String>.oxygenGeneratorRating(): Int {
    val bitLength = this.first().length
    return (0 until bitLength).fold(this) { remainingNumbers, i ->
        val frequencies = remainingNumbers.frequencies()
        val positionFrequency = frequencies[i]
        remainingNumbers
            .filter { it[i].toString().toInt() == positionFrequency.moreFrequent() }
            .ifEmpty { remainingNumbers }
    }.first().toInt(2)
}

private fun List<String>.scrubberRating(): Int {
    val bitLength = this.first().length
    return (0 until bitLength).fold(this) { remainingNumbers, i ->
        val frequencies = remainingNumbers.frequencies()
        val positionFrequency = frequencies[i]
        remainingNumbers
            .filter { it[i].toString().toInt() == positionFrequency.lessFrequent() }
            .ifEmpty { remainingNumbers }
    }.first().toInt(2)
}

private fun test(){

}