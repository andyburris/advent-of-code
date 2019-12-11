package com.andb.adventofcode.year2019.common

sealed class Opcode(val size: Int){
    class Add : Opcode(4)
    class Multiply : Opcode(4)
    class InputTo : Opcode(2)
    class OutputFrom : Opcode(2)
    class JumpIfTrue : Opcode(3)
    class JumpIfFalse : Opcode(3)
    class LessThan : Opcode(4)
    class Equals : Opcode(4)
    class RelativeBase : Opcode(2)
    class Stop : Opcode(1)
    class Error : Opcode(0)
}
