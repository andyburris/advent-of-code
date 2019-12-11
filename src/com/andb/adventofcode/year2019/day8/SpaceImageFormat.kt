package com.andb.adventofcode.year2019.day8

import java.io.File

private val reader = File("src/com/andb/adventofcode/year2019/day8/input.txt").bufferedReader()
private val testReader = File("src/com/andb/adventofcode/year2019/day8/test.txt").bufferedReader()

fun main(){
    partTwo()
}

private fun partOne(){
    val width = 25
    val height = 6
    val digits = reader.readLine().toCharArray().map { it.toString().toInt() }
    val toRows = digits.splitEvery(width).map { Row(it.toMutableList()) }
    val toLayers = toRows.splitEvery(height).map { Layer(it.toMutableList()) }
    val layerWithLeast = toLayers.minBy { it.toIntList().count { it == 0 } }!!
    println(layerWithLeast.toIntList().count { it == 1 } * layerWithLeast.toIntList().count { it == 2 })
}

private fun partTwo(){
    val width = 25
    val height = 6
    val digits = reader.readLine().toCharArray().map { it.toString().toInt() }
    val toRows = digits.splitEvery(width).map { Row(it.toMutableList()) }
    val toLayers = toRows.splitEvery(height).map { Layer(it.toMutableList()) }
    var layer = toLayers[0]
    for (l in toLayers){
        layer = layer.fillTransparentWith(l)
    }
    println(layer)
}

private fun test(){
    val width = 2
    val height = 2
    val digits = testReader.readLine().toCharArray().map { it.toString().toInt() }
    val toRows = digits.splitEvery(width).map { Row(it.toMutableList()) }
    val toLayers = toRows.splitEvery(height).map { Layer(it.toMutableList()) }
    var layer = toLayers[0]
    for (l in toLayers){
        layer = layer.fillTransparentWith(l)
    }
    println(layer)
}

data class Layer(val rows: MutableList<Row>){
    fun toIntList() = rows.flatMap { it.data }

    fun fillTransparentWith(other: Layer): Layer{
        val thisList = this.toIntList().toMutableList()
        val otherList = other.toIntList()
        for ((i, e) in thisList.withIndex()){
            if(e == 2){
                thisList[i] = otherList[i]
            }
        }
        return thisList.toLayers(rows[0].data.size, rows.size)[0]
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (r in rows){
            for (i in r.data){
                sb.append(if(i == 1) "█" else if(i == 0) " " else "2")
            }
            sb.append("\n")
        }
        return sb.toString()
    }
}
data class Row(val data: MutableList<Int>)

fun List<Int>.toLayers(width: Int, height: Int) = this
    .splitEvery(width)
    .map { Row(it.toMutableList()) }
    .splitEvery(height)
    .map { Layer(it.toMutableList()) }

fun <T> List<T>.splitEvery(increment: Int): List<List<T>>{
    val outList: MutableList<MutableList<T>> = mutableListOf()
    for ((i, e) in this.withIndex()){
        if(i%increment == 0){
            outList.add(mutableListOf(e))
        }else{
            outList.last().add(e)
        }
    }
    return outList
}