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

package Decoder

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
class MemoryReadPort extends Bundle {
  val inst = Input(UInt(16.W))
  val memoryRead = Output(Bool())
  val addressOffset = Output(UInt(8.W))
}

class MemoryRead extends Module {
  val io = IO(new MemoryReadPort)

  io.addressOffset(1,0) := 0x0.U(2.W)

  when(io.inst(15, 13) === 0x2.U(3.W) && io.inst(1,0) === 0x2.U(2.W)) {
    //LWSP
    io.memoryRead := true.B
    io.addressOffset(7, 6) := io.inst(3, 2)
    io.addressOffset(5) := io.inst(12)
    io.addressOffset(4, 2) := io.inst(6, 4)
  }.elsewhen(io.inst(15, 13) === 0x2.U(3.W) && io.inst(1,0) === 0x0.U(2.W)) {
    //LW
    io.memoryRead := true.B
    io.addressOffset(7) := 0x0.U(1.W)
    io.addressOffset(6) := io.inst(5)
    io.addressOffset(5, 3) := io.inst(12, 10)
    io.addressOffset(2) := io.inst(6)
  }.otherwise{
    io.memoryRead := false.B
    io.addressOffset(7,2) := 0x0.U(6.W)
  }
}
