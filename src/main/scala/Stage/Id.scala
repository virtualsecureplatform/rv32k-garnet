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

class IdPort extends Bundle {
  val inst = Input(UInt(16.W))
  val wbClock = Input(Clock())

  val rs1Data = Output(UInt(32.W))
  val rs2Data = Output(UInt(32.W))
  val immData = Output(SInt(32.W))
  val debugPort = Output(UInt(32.W))

  val opcodeALU = Output(UInt(1.W))
  val selImm = Output(Bool())

  val memRead = Output(Bool())
  val memWrite = Output(Bool())
  val memWriteData = Output(UInt(32.W))

  val regWriteData = Input(UInt(32.W))
}

class Id extends Module{
  val io = IO(new IdPort)

  val mainRegister = Module(new Memory.Register)
  mainRegister.clock := io.wbClock
  mainRegister.io.writeEnable := true.B

  val registerDecoder = Module(new Decoder.Register)
  val aluDecoder = Module(new Decoder.ALU)
  val immDecoder = Module(new Decoder.Imm)
  val memReadDecoder = Module(new Decoder.MemoryRead)
  val memWriteDecoder = Module(new Decoder.MemoryWrite)

  registerDecoder.io.inst := io.inst
  aluDecoder.io.inst := io.inst
  immDecoder.io.inst := io.inst

  mainRegister.io.rs1 := registerDecoder.io.rs1
  mainRegister.io.rs2 := registerDecoder.io.rs2
  mainRegister.io.rd := registerDecoder.io.rd
  mainRegister.io.writeData := io.regWriteData

  io.rs1Data := mainRegister.io.rs1Data
  io.rs2Data := mainRegister.io.rs2Data
  io.debugPort := mainRegister.io.x15Data
  io.opcodeALU := aluDecoder.io.opcode
  io.selImm := (immDecoder.io.sel || memReadDecoder.io.memoryRead || memWriteDecoder.io.memoryWrite)
  io.memRead := memReadDecoder.io.memoryRead
  io.memWrite := memWriteDecoder.io.memoryWrite

  io.memWriteData := mainRegister.io.rs2Data

  when(immDecoder.io.sel){
    io.immData := immDecoder.io.out.asSInt()
  }.elsewhen(memReadDecoder.io.memoryRead){
    io.immData := memReadDecoder.io.addressOffset.asSInt()
  }.elsewhen(memWriteDecoder.io.memoryWrite){
    io.immData := memWriteDecoder.io.addressOffset.asSInt()
  }.otherwise{
    io.immData := DontCare
  }
}
