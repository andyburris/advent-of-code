package com.andb.adventofcode.year2019.day7

import com.andb.adventofcode.year2019.common.Intcode

class Amplifier(val id: Int, program: MutableList<Long>) : Intcode(program) {
    lateinit var nextLink: Amplifier

    override var output: Long = 0
        set(value){
            field = value
            allOutputs.add(value)
            nextLink.input.add(value)
            //println("outputting $value to amp ${nextLink.id}")
            //println("products recieved? amp ${nextLink.id}.input = ${nextLink.input}")
        }

    override fun toString(): String {
        return "Amp $id - ${super.toString()}"
    }
}

fun Collection<Number>.toAmplifier(id: Int) = Amplifier(id, this.map { it.toLong() }.toMutableList())
fun Intcode.toAmplifier(id: Int) = Amplifier(id, program)
