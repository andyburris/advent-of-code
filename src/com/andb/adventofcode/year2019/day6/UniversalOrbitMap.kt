package com.andb.adventofcode.year2019.day6

import java.io.File

private val reader = File("src/com/andb/adventofcode/year2019/day6/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day6/test.txt").bufferedReader()

fun main(){
    partTwo()
}

private fun partOne(){
    val orbitNotations = reader.readLines().map { val list = it.split(')'); OrbitNotation(list[0], list[1]) }
    val all = allObjects(orbitNotations)

    println(all.sumBy { it.parentCount() })
}

private fun partTwo(){
    val orbitNotations = reader.readLines().map { val list = it.split(')'); OrbitNotation(list[0], list[1]) }
    val all = allObjects(orbitNotations)

    val you = all.first { it.name == "YOU" }
    val san = all.first { it.name == "SAN" }

    val overlap = you.parents().intersect(san.parents())
    val firstIntersection = overlap.maxBy { it.parentCount() }!!
    //println(firstIntersection)
    println(you.transfersUntil(firstIntersection) + san.transfersUntil(firstIntersection))
}


private fun allObjects(orbitNotations: List<OrbitNotation>): List<SpaceObject2>{
    val list = mutableListOf<SpaceObject2>()
    var level = listOf<SpaceObject2>(SpaceObject2("COM", null))
    while (level.isNotEmpty()){
        val temp = level.toList()
        level = orbitNotations.filter { it.center in temp.map { it.name } }.map { notation-> SpaceObject2(notation.orbiter, temp.find { it.name == notation.center }) }
        list.addAll(temp)
    }
    return list
}

data class OrbitNotation(val center: String, val orbiter: String){
    override fun toString(): String {
        return "$center)$orbiter"
    }
}
data class SpaceObject2(val name: String, val parent: SpaceObject2?){
    fun parents(): List<SpaceObject2> = if(parent == null) emptyList() else mutableListOf(parent).also { it.addAll(parent.parents()) }
    fun parentCount(): Int = if(parent == null) 0 else 1 + parent.parentCount()
    fun transfersUntil(object2: SpaceObject2): Int = if (parent == object2) 0 else 1 + parent!!.transfersUntil(object2)
}