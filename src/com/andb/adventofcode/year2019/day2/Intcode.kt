package com.andb.adventofcode.year2019.day2

class Intcode : ArrayList<Int>(){

    var currentPointer = 0

    fun input(input1: Int, input2: Int){
        this[1] = input1
        this[2] = input2
    }

    fun output() = this[0]

    fun run(): Int{
        var flag = true
        while (flag) {
            val opcode = this[currentPointer]
            when (opcode) {
                1 -> add(this[currentPointer+1], this[currentPointer+2], this[currentPointer+3])
                2 -> multiply(this[currentPointer+1], this[currentPointer+2], this[currentPointer+3])
                99 -> flag = false
            }
            currentPointer += 4
        }
        return output()
    }

    fun add(pos1: Int, pos2: Int, storePos: Int){
        this[storePos] = this[pos1] + this[pos2]
    }

    fun multiply(pos1: Int, pos2: Int, storePos: Int){
        this[storePos] = this[pos1] * this[pos2]
    }
}

fun Collection<Int>.toIntcode() = this.toCollection(Intcode())