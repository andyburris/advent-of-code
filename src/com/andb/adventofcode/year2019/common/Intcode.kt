package com.andb.adventofcode.year2019.common

import java.lang.StringBuilder

open class Intcode : ArrayList<Int>() {

    var currentPointer = 0
    var input = mutableListOf<Int>(1)
    open var output = 0

    fun inputIntoCode(input1: Int, input2: Int) {
        this[1] = input1
        this[2] = input2
    }

    fun outputFromCode() = this[0]

    fun run(): Int {
        var flag = true
        //println("opcode: ${this[currentPointer]%100}, inputs: $input, $this")
        while (flag) {
            val (opcode, params) = getOpcodeAndParams()
            when (opcode) {
                is Opcode.Add -> { this[params[2]] = this[params[0]] + this[params[1]] }
                is Opcode.Multiply -> { this[params[2]] = this[params[0]] * this[params[1]] }
                is Opcode.InputTo -> { if(input.isNotEmpty()) this[params[0]] = input.removeAt(0) else currentPointer-= opcode.size}
                is Opcode.OutputFrom ->{ this.output = this[params[0]] }
                is Opcode.JumpIfTrue -> { if(this[params[0]] != 0){ currentPointer = this[params[1]] - opcode.size } }
                is Opcode.JumpIfFalse -> { if(this[params[0]] == 0){ currentPointer = this[params[1]] - opcode.size } }
                is Opcode.LessThan -> { this[params[2]] = if(this[params[0]] < this[params[1]]) 1 else 0 }
                is Opcode.Equals -> { this[params[2]] = if(this[params[0]] == this[params[1]]) 1 else 0 }
                else -> { flag = false; }
            }
            currentPointer += opcode.size
            //println("opcode: ${this[currentPointer]%100}, inputs: $input, $this")

        }
        return output
    }

    fun getOpcodeAndParams(): Pair<Opcode, List<Parameter>>{
        val instruction = this[currentPointer]
        val code = instruction%100
        val opcode = when(code){
            1->Opcode.Add()
            2->Opcode.Multiply()
            3->Opcode.InputTo()
            4->Opcode.OutputFrom()
            5->Opcode.JumpIfTrue()
            6->Opcode.JumpIfFalse()
            7->Opcode.LessThan()
            8->Opcode.Equals()
            99-> return Pair(Opcode.Stop(), listOf())
            else-> return Pair(Opcode.Error(), listOf())
        }
        val values = this.slice(currentPointer+1..currentPointer+opcode.size)
        val modes = (instruction/100).toDigits().reversed().extendTo(opcode.size)
        val params = (0 until opcode.size-1).map { Parameter(modes[it], values[it]) }
        //println("params for $instruction: $params")
        return Pair(opcode, params)
    }

    operator fun MutableList<Int>.get(parameter: Parameter): Int{
        return if(parameter.mode == 0) this[parameter.value] else parameter.value
    }

    operator fun MutableList<Int>.set(parameter: Parameter, value: Int){
        this[parameter.value] = value
    }

    override fun toString(): String {
        val str = super.toString()
        return "@ $currentPointer, intcode is $str"
    }
}

data class Parameter(val mode: Int, val value: Int)

fun Collection<Int>.toIntcode() = this.toCollection(Intcode())
