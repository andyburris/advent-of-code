package com.andb.adventofcode.year2019.common

import kotlinx.coroutines.*

fun Int.toDigits(): List<Int> = this.toString().toCharArray().map { it.toString().toInt() }

fun List<Int>.extendTo(to: Int, insert: Int = 0): List<Int> {
    val mutable = this.toMutableList()
    for (i in size until to) {
        mutable.add(insert)
    }
    return mutable
}

fun newIOThread(block: suspend CoroutineScope.() -> Unit) = CoroutineScope(Dispatchers.IO).launch { block.invoke(this) }

fun newThread(block: suspend CoroutineScope.() -> Unit): Job = CoroutineScope(Dispatchers.Default).launch { block.invoke(this) }