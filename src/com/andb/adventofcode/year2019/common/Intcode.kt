package com.andb.adventofcode.year2019.common

class Intcode : ArrayList<Int>() {

    var currentPointer = 0
    var input = 1
    var output = 0

    fun inputIntoCode(input1: Int, input2: Int) {
        this[1] = input1
        this[2] = input2
    }

    fun outputFromCode() = this[0]

    fun run(): Int {
        var flag = true
        println("at pointer $currentPointer, intcode is $this")
        while (flag) {
            val instruction = this[currentPointer]
            val opcode = instruction % 100
            println("at pointer $currentPointer (${this[currentPointer]}), opcode is $opcode")
            //Needs to be when-then-currentPointer+= because the pointer-changing opcodes won't work otherwise
            val increment = when (opcode) {
                1 -> Opcode.Add(instruction).run(this)
                2 -> Opcode.Multiply(instruction).run(this)
                3 -> Opcode.InputTo(instruction).run(this)
                4 -> Opcode.OutputFrom(instruction).run(this)
                5 -> Opcode.JumpIfTrue(instruction).run(this)
                6 -> Opcode.JumpIfFalse(instruction).run(this)
                7 -> Opcode.LessThan(instruction).run(this)
                8 -> Opcode.Equals(instruction).run(this)
                else -> {
                    flag = false; 1
                }
            }
            currentPointer += increment
            println("at pointer $currentPointer, intcode is $this")
        }
        return output
    }
}

fun Collection<Int>.toIntcode() = this.toCollection(Intcode())
