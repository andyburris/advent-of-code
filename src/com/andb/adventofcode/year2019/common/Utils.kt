package com.andb.adventofcode.year2019.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.awt.Color
import java.io.BufferedReader
import kotlin.math.pow
import kotlin.random.Random

fun BufferedReader.toIntcode() = this.readLine().split(",").map { it.toInt() }.toIntcode()

/* Number Utils */
fun Int.toDigits(): List<Int> = this.toString().toCharArray().map { it.toString().toInt() }
fun Int.pow(exponent: Int) = this.toDouble().pow(exponent)
infix fun Int.divCeil(other: Int) = if (this%other ==0) this/other else this/other + 1
infix fun Long.divCeil(other: Long) = if (this%other == 0L) this/other else this/other + 1

/* Coroutine Utils */
fun newIOThread(block: suspend CoroutineScope.() -> Unit) = CoroutineScope(Dispatchers.IO).launch { block.invoke(this) }
fun newThread(block: suspend CoroutineScope.() -> Unit): Job = CoroutineScope(Dispatchers.Default).launch { block.invoke(this) }


/* Collection Utils */
fun List<Int>.extendTo(to: Int, insert: Int = 0): List<Int> {
    if(to > this.size){
        val mutable = this.toMutableList()
        for (i in size until to) {
            mutable.add(insert)
        }
        return mutable
    }else{
        return this
    }
}

fun <T> MutableList<T>.extendToApply(to: Int, insert: T){
    if(to > this.size){
        for (i in size until to) {
            this.add(insert)
        }
    }
}

fun <K, V> MutableMap<K, V>.removeAll(keys: List<K>){
    for (k in keys){
        this.remove(k)
    }
}

public inline fun <K, V> Map<K,V>.sumBy(block: (Map.Entry<K, V>)->Int): Int{
    var sum = 0
    this.forEach { sum+=block.invoke(it) }
    return sum
}
public inline fun <K, V> Map<K,V>.sumByLong(block: (Map.Entry<K, V>)->Long): Long{
    var sum = 0L
    this.forEach { sum+=block.invoke(it) }
    return sum
}

fun randomColor(): Color? {
    val r: Int = Random.nextInt(0, 255)
    val g: Int = Random.nextInt(0, 255)
    val b: Int = Random.nextInt(0, 255)
    return Color(r, g, b)
}