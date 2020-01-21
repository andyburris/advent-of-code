package com.andb.adventofcode.year2019.day14

import com.andb.adventofcode.year2019.common.DefaultHashMap
import com.andb.adventofcode.year2019.common.divCeil
import com.andb.adventofcode.year2019.common.sumBy
import com.andb.adventofcode.year2019.common.sumByLong
import java.io.File

private val reader = File("src/com/andb/adventofcode/year2019/day14/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day14/test.txt").bufferedReader()

fun main(){
    partTwo()
}

private fun partOne(){
    val reactions = reader.readLines().map { it.toReaction() }
    println(reactions.joinToString("\n"))
    println()

    println(makeFuel(1, reactions))
}

private fun partTwo(){
    val reactions = testReader.readLines().map { it.toReaction() }
    var amount = 0L
    var increment = 100000
    var oreNeeded = 0L
    while (oreNeeded < 1000000000000){
        println("attempt $amount")
        amount+=increment
        val newOre = makeFuel(amount, reactions)
        if (newOre >= 1000000000000 && increment > 1){
            amount-=increment
            increment/=10
        }else{
            oreNeeded = newOre
        }
    }
    println(amount - 1)
}

private fun makeFuel(amount: Long, reactions: List<Reaction>): Long{
    val remaining = DefaultHashMap<String, Long>(0)
    var oreNeeded = DefaultHashMap<String, Long>(0)

    fun getRawFor(material: String, amount: Long){
        //println("getting raw materials for $amount $material")
        val neededToCreate = amount - remaining[material]
        if (neededToCreate > 0){
            val reactionToCreate = reactions.find { it.products.material == material }!!
            val multipleToCreate = neededToCreate divCeil reactionToCreate.products.amount.toLong()
            //println("to create $neededToCreate $material, need $multipleToCreate of $reactionToCreate")
            reactionToCreate.reactants.forEach {
                if (it.material == "ORE"){
                    //println("using ${it.amount * multipleToCreate} ore to create $neededToCreate $material")
                    oreNeeded[material] += it.amount * multipleToCreate
                }else{
                    getRawFor(it.material, it.amount * multipleToCreate)
                }
            }
            remaining[material]+=reactionToCreate.products.amount * multipleToCreate
        }
        remaining[material]-=amount
    }

    getRawFor("FUEL", amount)
    //println("ore breakdown: $oreNeeded")
    return oreNeeded.sumByLong { it.value }
}

private fun test(){

}

private fun String.toReaction(): Reaction{
    val (inputStr, outputStr) = this.split(" => ")
    val inputs = inputStr.split(", ")
    val inputMaterials: List<Specie> = inputs.map {
        val (number, string) = it.split(" ")
        Specie(string, number.toInt())
    }
    val (outNumber, outString) = outputStr.split(" ")
    val outputMaterial = Specie(outString, outNumber.toInt())
    return Reaction(inputMaterials, outputMaterial)
}

private class Reaction(val reactants: List<Specie>, val products: Specie){
    override fun toString(): String = "${reactants.map { "${it.amount} ${it.material}" }.joinToString(separator = ", ")} => ${products.amount} ${products.material}"
}

class Specie(val material: String, val amount: Int)