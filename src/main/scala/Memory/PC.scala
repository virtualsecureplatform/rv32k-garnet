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

package Memory

import chisel3._

class PCPort extends Bundle {
  val jumpAddress = Input(UInt(32.W))
  val jump = Input(Bool())
  val pcOut = Output(UInt(32.W))
}

class PC extends Module{
  val io = IO(new PCPort)
  val RegPC = RegInit(0.U(32.W))
  when(io.jump === false.B){
    RegPC := RegPC+1.U(32.W)
  }.otherwise{
    RegPC := io.jumpAddress
  }

  io.pcOut := RegPC
}
