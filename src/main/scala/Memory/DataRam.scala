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
import chisel3.util._

class DataRamPort extends Bundle {
  val readAddress = Input(UInt(10.W))

  val writeAddress = Input(UInt(10.W))
  val writeData = Input(UInt(32.W))
  val writeEnable = Input(Bool())

  val out = Output(UInt(32.W))
}
class DataRam extends Module{
  val io = IO(new DataRamPort)

  val memory = Mem(1024, UInt(32.W))

  when(io.writeEnable){
    memory(io.writeAddress) := io.writeData
  }

  io.out := memory(io.readAddress)
}
