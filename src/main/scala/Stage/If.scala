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

class IfPort extends Bundle {
  val jumpAddress = Input(UInt(32.W))
  val jump = Input(Bool())

  val instOut = Output(UInt(16.W))
}

class If extends Module{
  val io = IO(new IfPort)

  val instRom = Module(new Memory.InstRom)
  val pc = Module(new Memory.PC)

  pc.io.jumpAddress := io.jumpAddress
  pc.io.jump := io.jump

  instRom.io.readAddress := pc.io.pcOut(3, 0)

  io.instOut := instRom.io.out
}
