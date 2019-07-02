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

class MemoryWritePort extends Bundle {
  val inst = Input(UInt(16.W))
  val memoryWrite = Output(Bool())
  val addressOffset = Output(UInt(8.W))
}

class MemoryWrite extends Module{
  val io = IO(new MemoryWritePort)

  io.addressOffset(1,0) := 0x0.U(2.W)

  when(io.inst(15, 13) === 0x6.U(3.W) && io.inst(1,0) === 0x2.U(2.W)) {
    //SWSP
    io.memoryWrite := true.B
    io.addressOffset(7, 6) := io.inst(8, 7)
    io.addressOffset(5, 2) := io.inst(12, 9)
  }.elsewhen(io.inst(15, 13) === 0x6.U(3.W) && io.inst(1,0) === 0x0.U(2.W)) {
    //SW
    io.memoryWrite := true.B
    io.addressOffset(7) := 0x0.U(1.W)
    io.addressOffset(6) := io.inst(5)
    io.addressOffset(5, 3) := io.inst(12, 10)
    io.addressOffset(2) := io.inst(6)
  }.otherwise{
    io.memoryWrite := false.B
    io.addressOffset(7,2) := 0x0.U(6.W)
  }
}
