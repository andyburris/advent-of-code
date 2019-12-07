package com.andb.adventofcode.year2019.day7

import com.andb.adventofcode.year2019.common.Intcode

class Amplifier(val id: Int) : Intcode() {
    lateinit var nextLink: Amplifier

    override var output: Int = 0
        set(value){
            field = value
            nextLink.input.add(value)
            //println("outputting $value to amp ${nextLink.id}")
            //println("output recieved? amp ${nextLink.id}.input = ${nextLink.input}")
        }

    override fun toString(): String {
        return "Amp $id - ${super.toString()}"
    }
}

fun Collection<Int>.toAmplifier(id: Int) = this.toCollection(Amplifier(id))
