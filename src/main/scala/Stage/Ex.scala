/*
Copyright 2019 Naoki Matsumoto

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package Stage

import chisel3._

class ExPort extends Bundle {
  val rs1Data = Input(UInt(32.W))
  val rs2Data = Input(UInt(32.W))
  val immData = Input(SInt(32.W))
  val selImm = Input(Bool())
  val opcodeALU = Input(UInt(1.W))

  val memReadIn = Input(Bool())
  val memWriteIn = Input(Bool())
  val memWriteDataIn = Input(UInt(32.W))

  val memReadOut = Output(Bool())
  val memWriteOut = Output(Bool())
  val memWriteDataOut = Output(UInt(32.W))

  val out = Output(UInt(32.W))
}


class Ex extends Module{
  val io = IO(new ExPort)

  val alu = Module(new ALU.Adder)

  alu.io.in_a := io.rs1Data.asSInt()
  alu.io.sel := io.opcodeALU
  io.out := alu.io.out.asUInt()

  when(io.selImm) {
    alu.io.in_b := io.immData
  }.otherwise{
    alu.io.in_b := io.rs2Data.asSInt()
  }

  io.memReadOut := io.memReadIn
  io.memWriteOut := io.memWriteIn
  io.memWriteDataOut := io.memWriteDataIn
}
