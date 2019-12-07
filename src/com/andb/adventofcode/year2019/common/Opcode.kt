package com.andb.adventofcode.year2019.common

sealed class Opcode(val instruction: Int, val paramsSize: Int, val incrementPointer: Boolean = true){

    fun run(intcode: Intcode): Int{
        runWithParams(intcode, resolveParams(intcode))
        return increment()
    }
    abstract fun runWithParams(intcode: Intcode, params: List<Int>);

    fun modes() = instruction/100
    fun increment() = paramsSize + 1

    fun resolveParams(intcode: Intcode): List<Int>{
        println("resolving params for instruction ${intcode.slice(intcode.currentPointer..intcode.currentPointer+paramsSize)}")
        val modeList = modes().toDigits().reversed().extendTo(paramsSize)
        val params = mutableListOf<Int>()
        for (i in 0 until paramsSize){
            val number = intcode[intcode.currentPointer + i + 1]
            val mode = modeList[i]
            val resolved = if(mode == 0 && i!=paramsSize-1) intcode[number] else number
            params.add(resolved)
        }
        return params
    }

    class Add(instruction: Int) : Opcode(instruction, 3){
        override fun runWithParams(intcode: Intcode, params: List<Int>) {
            intcode[params[2]] = params[0] + params[1]
            println("adding ${params[0]} + ${params[1]} into position ${params[2]}")
        }
    }

    class Multiply(instruction: Int) : Opcode(instruction, 3){
        override fun runWithParams(intcode: Intcode, params: List<Int>) {
            intcode[params[2]] = params[0] * params[1]
        }
    }

    class InputTo(instruction: Int) : Opcode(instruction, 1){
        override fun runWithParams(intcode: Intcode, params: List<Int>) {
            intcode[params[0]] = intcode.input
        }
    }

    class OutputFrom(instruction: Int) : Opcode(instruction, 1){
        override fun runWithParams(intcode: Intcode, params: List<Int>) {
            intcode.output = params[0]
        }
    }


    class JumpIfTrue(instruction: Int) : Opcode(instruction, 2){
        override fun runWithParams(intcode: Intcode, params: List<Int>) {
            if(params[0] != 0){
                println("currentPtr: ${intcode.currentPointer}")
                intcode.currentPointer = params[1] - increment()
                println("currentPtr: ${intcode.currentPointer}")
            }
        }
    }

    class JumpIfFalse(instruction: Int) : Opcode(instruction, 2){
        override fun runWithParams(intcode: Intcode, params: List<Int>) {
            if(params[0] == 0){
                println("currentPtr: ${intcode.currentPointer}")
                intcode.currentPointer = params[1] - increment()
                println("currentPtr: ${intcode.currentPointer}")
            }
        }
    }

    class LessThan(instruction: Int) : Opcode(instruction, 3){
        override fun runWithParams(intcode: Intcode, params: List<Int>) {
            intcode[params[2]] = if(params[0] < params[1]) 1 else 0
        }
    }

    class Equals(instruction: Int) : Opcode(instruction, 3){
        override fun runWithParams(intcode: Intcode, params: List<Int>) {
            intcode[params[2]] = if(params[0] == params[1]) 1 else 0
        }
    }

}
