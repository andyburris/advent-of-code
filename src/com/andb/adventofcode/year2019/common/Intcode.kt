package com.andb.adventofcode.year2019.common

open class Intcode(val program: MutableList<Long>) {

    var currentPointer = 0L
    var relativeBase = 0L
    var input = mutableListOf<Long>(1)
    open var output = 0L
        set(value) {
            field = value
            allOutputs.add(value)
            onOutput.invoke(value)
        }

    val allOutputs = mutableListOf<Long>()
    var onOutput: (output: Long)->Unit = {}

    @Deprecated("Use input/output variables instead")
    fun inputIntoCode(input1: Int, input2: Int) {
        program[1] = input1.toLong()
        program[2] = input2.toLong()
    }

    @Deprecated("Use input/output variables instead")
    fun outputFromCode() = program[0].toInt()

    fun run(): Long {
        var flag = true
        //println("opcode: ${program[currentPointer]%100}, inputs: $input, $this")
        while (flag) {
            val (opcode, params) = getOpcodeAndParams()
            when (opcode) {
                is Opcode.Add -> { program[params[2]] = program[params[0]] + program[params[1]] }
                is Opcode.Multiply -> { program[params[2]] = program[params[0]] * program[params[1]] }
                is Opcode.InputTo -> { if(input.isNotEmpty()) program[params[0]] = input.removeAt(0) else currentPointer-= opcode.size}
                is Opcode.OutputFrom ->{ this.output = program[params[0]] }
                is Opcode.JumpIfTrue -> { if(program[params[0]] != 0L){ currentPointer = program[params[1]] - opcode.size } }
                is Opcode.JumpIfFalse -> { if(program[params[0]] == 0L){ currentPointer = program[params[1]] - opcode.size } }
                is Opcode.LessThan -> { program[params[2]] = if(program[params[0]] < program[params[1]]) 1 else 0 }
                is Opcode.Equals -> { program[params[2]] = if(program[params[0]] == program[params[1]]) 1 else 0 }
                is Opcode.RelativeBase -> { relativeBase += program[params[0]]}
                else -> { flag = false; }
            }
            currentPointer += opcode.size
            //println("opcode: ${program[currentPointer]%100}, inputs: $input, $this")

        }
        return output
    }

    fun getOpcodeAndParams(): Pair<Opcode, List<Parameter>>{
        val instruction = program[currentPointer.toInt()]
        val code = instruction%100
        val opcode = when(code.toInt()){
            1->Opcode.Add()
            2->Opcode.Multiply()
            3->Opcode.InputTo()
            4->Opcode.OutputFrom()
            5->Opcode.JumpIfTrue()
            6->Opcode.JumpIfFalse()
            7->Opcode.LessThan()
            8->Opcode.Equals()
            9->Opcode.RelativeBase()
            99-> return Pair(Opcode.Stop(), listOf())
            else-> return Pair(Opcode.Error(), listOf())
        }
        val values = program.slice((currentPointer+1).toInt()..(currentPointer+opcode.size).toInt())
        val modes = (instruction/100).toInt().toDigits().reversed().extendTo(opcode.size)
        val params = (0 until opcode.size-1).map { Parameter(modes[it], values[it]) }
        //println("params for $instruction: $params")
        return Pair(opcode, params)
    }

    operator fun MutableList<Long>.get(parameter: Parameter): Long{
        return when(parameter.mode){
            0-> { extendToApply(parameter.value.toInt() + 1, 0); program[parameter.value.toInt()] }
            1-> parameter.value
            else -> { extendToApply(parameter.value.toInt() + 1, 0); program[(relativeBase + parameter.value).toInt()] }
        }
    }

    operator fun MutableList<Long>.set(index: Parameter, element: Number){
        extendToApply((index.value + relativeBase.coerceAtLeast(0) + 1).toInt(), 0)
        when(index.mode){
            0, 1 -> program[index.value.toInt()] = element.toLong()
            2->program[relativeBase.toInt() + index.value.toInt()] = element.toLong()
        }

    }

    override fun toString(): String {
        val str = super.toString()
        return "@ $currentPointer, intcode is $str"
    }
}

data class Parameter(val mode: Int, val value: Long)

fun Collection<Number>.toIntcode() = Intcode(this.map { it.toLong() }.toMutableList())
fun Intcode.clone() = Intcode(this.program)
