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

import Config.Instructions
import chisel3._
import chisel3.util.Cat

class MemoryReadPort extends Bundle {
  val inst = Input(UInt(16.W))
  val memoryRead = Output(Bool())
  val addressOffset = Output(UInt(8.W))
}

class MemoryRead extends Module {
  val io = IO(new MemoryReadPort)

  when(io.inst === Instructions.LWSP)  {
    //LWSP
    io.memoryRead := true.B
    io.addressOffset := Cat(io.inst(3,2), io.inst(12), io.inst(6, 4), 0x0.U(2.W))
  }.elsewhen(io.inst === Instructions.LW) {
    //LW
    io.memoryRead := true.B
    io.addressOffset := Cat(0x0.U(1.W), io.inst(5), io.inst(12,10), io.inst(6), 0x0.U(2.W))
  }.otherwise{
    io.memoryRead := false.B
    io.addressOffset := 0x0.U(8.W)
  }

}
