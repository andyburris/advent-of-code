package com.andb.adventofcode.year2019.common

import kotlinx.coroutines.*
import kotlin.math.pow

fun Int.toDigits(): List<Int> = this.toString().toCharArray().map { it.toString().toInt() }
fun Int.pow(exponent: Int) = this.toDouble().pow(exponent)

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

fun newIOThread(block: suspend CoroutineScope.() -> Unit) = CoroutineScope(Dispatchers.IO).launch { block.invoke(this) }

fun newThread(block: suspend CoroutineScope.() -> Unit): Job = CoroutineScope(Dispatchers.Default).launch { block.invoke(this) }