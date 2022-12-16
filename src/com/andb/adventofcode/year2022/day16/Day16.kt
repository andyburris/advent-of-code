package com.andb.adventofcode.year2022.day16

import java.io.File

private val reader = File("src/com/andb/adventofcode/year2022/day16/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2022/day16/test.txt").bufferedReader()

fun main(){
    partOne()
}

private fun partOne(){
    val valves = testReader.readLines().map { it.toValve() }
    println(valves)
    val minutes = (1..30)

/*
    minutes.fold(emptyMap<String, Int>() to "AA") { (openedValves, currentLocation), minute ->
        val remainingMinutes = minutes.last - minute
        val currentValve = valves.first { it.id == currentLocation }
        val possibleMovements = currentValve.tunnels.map { tunnel -> valves.first { it.id == tunnel } }

        openedValves to currentLocation
    }
*/
    var remainingMinutes = minutes.last
    var currentLocation = valves.first { it.id == "AA" }
    val openedValves = mutableMapOf<Valve, Int>()
    while (remainingMinutes > 0) {
        val nextBestProduction =  currentLocation.bestProduction(remainingMinutes, valves, openedValves.keys.toList())
        currentLocation = valves.first { it.id == nextBestProduction.valveId }
        remainingMinutes -= nextBestProduction.minutesElapsed
        openedValves += currentLocation to remainingMinutes
    }
    println(openedValves)
}

private fun partTwo(){
}

private fun test(){

}

private data class Valve(val id: String, val flowRate: Int, val tunnels: List<String>) {
    fun potentialProduction(remainingMinutes: Int) = flowRate * remainingMinutes

    fun bestProduction(remainingMinutes: Int, allValves: List<Valve>, openedValves: List<Valve>): ProductionPotential {
        if (remainingMinutes == 1) return ProductionPotential(this.id, 0, remainingMinutes)
        val thisProduction: Int = if (this !in openedValves) potentialProduction(remainingMinutes) else 0
/*
        val movingProduction = tunnels
            .map { tunnel -> allValves.find {it.id == tunnel} ?: throw Error("could not find tunnel id = $tunnel") }
            .map { valve -> valve.bestProduction(remainingMinutes - 1, allValves, openedValves).let { it.copy(minutesElapsed = it.minutesElapsed + 1) } }
        val bestMovingProduction = movingProduction.maxByOrNull { it.flowAmount } ?: ProductionPotential(0, 0, "")
        if (thisProduction > bestMovingProduction.flowAmount) return ProductionPotential(thisProduction, 1, this.id)
        return bestMovingProduction
*/

        val otherProductions = allValves.filter { it !in openedValves }.map{
            val pathLength = allValves.shortestPathBetween(this, it)
            val remainingMinutesAtValve = remainingMinutes - pathLength - 1
            val production = it.potentialProduction(remainingMinutesAtValve)
            ProductionPotential(it.id, production, pathLength + 1)
        }
        println("current valve = ${this.id}, remainingMinutes = $remainingMinutes, otherProductions = $otherProductions")
        val maxProduction = (otherProductions + ProductionPotential(this.id, thisProduction, 1)).maxByOrNull { it.flowAmount }!!
        return maxProduction
    }
}

private fun List<Valve>.shortestPathBetween(
    start: Valve,
    end: Valve,
    alreadySearched: List<Valve> = emptyList()
): Int {
    if (end.id in start.tunnels) return 1
    return start.tunnels
        .map { tunnel -> this.first { it.id == tunnel} }
        .filter { it !in alreadySearched }
        .minOfOrNull { this.shortestPathBetween(it, end, alreadySearched + start) + 1 } ?: 1000000
}

data class ProductionPotential(
    val valveId: String,
    val flowAmount: Int,
    val minutesElapsed: Int,
)
private fun String.toValve(): Valve {
    val id = this.removePrefix("Valve ").take(2)
    val flowRate = this.removePrefix("Valve $id has flow rate=").takeWhile { it in '0'..'9' }.toInt()
    val tunnels = this.removePrefix("Valve $id has flow rate=$flowRate; tunnels lead to valves ").split(", ")

    return Valve(id, flowRate, tunnels)
}