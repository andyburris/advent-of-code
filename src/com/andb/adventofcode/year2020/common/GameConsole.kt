package com.andb.adventofcode.year2020.common

data class Instruction(val index: Int, val operation: Operation, val argument: Int)
fun String.toInstruction(index: Int): Instruction {
    val (operation, argument) = this.split(" ")
    return Instruction(index, operation.toOperation(), argument.toInt())
}

enum class Operation {
    Accumulator,
    Jump,
    NoOp,
}
fun String.toOperation() = when(this) {
    "acc" -> Operation.Accumulator
    "jmp" -> Operation.Jump
    "nop" -> Operation.NoOp
    else -> throw Error("String should only be one of \"acc\", \"jmp\", or \"nop\"")
}

sealed class ConsoleResult {
    abstract val accumulated: Int

    data class Success(override val accumulated: Int) : ConsoleResult()
    sealed class Exception : ConsoleResult() {
        data class Loop(override val accumulated: Int, val stacktrace: List<Int>) : Exception()
    }
}

fun List<Instruction>.execute(accumulatorStart: Int = 0, startIndex: Int = 0): ConsoleResult {
    var accumulator: Int = accumulatorStart
    var instructionIndex = startIndex

    fun Instruction.execute() {
        when (this.operation) {
            Operation.Accumulator ->{
                accumulator += this.argument
                instructionIndex++
            }
            Operation.Jump -> instructionIndex += this.argument
            Operation.NoOp -> instructionIndex++
        }
    }

    val visitedIndexes = mutableListOf<Int>()
    while (instructionIndex < this.size) {
        if (instructionIndex !in visitedIndexes) {
            visitedIndexes.add(instructionIndex)
            val instruction = this[instructionIndex]
            instruction.execute()
            println("executed $instruction, new index = $instructionIndex, new accumulator = $accumulator")
        } else {
            return ConsoleResult.Exception.Loop(accumulator, visitedIndexes + instructionIndex)
        }
    }
    println("exited loop")
    return ConsoleResult.Success(accumulator)
}

fun Instruction.leadsTo(index: Int): Boolean = when (operation) {
    Operation.Accumulator, Operation.NoOp -> this.index + 1== index
    Operation.Jump -> this.index + this.argument == index
}

fun Instruction.flipped() = when (operation) {
    Operation.Jump -> this.copy(operation = Operation.NoOp)
    Operation.NoOp -> this.copy(operation = Operation.Jump)
    Operation.Accumulator -> this
}