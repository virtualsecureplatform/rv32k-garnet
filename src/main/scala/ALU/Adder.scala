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

package ALU

import chisel3._
import chisel3.util._

class AdderPort extends Bundle {
  val in_a = Input(SInt(32.W))
  val in_b = Input(SInt(32.W))
  val sel = Input(UInt(1.W))
  val out = Output(SInt(32.W))
}

class Adder extends Module {
  val io = IO(new AdderPort)

  when(io.sel === 0.U) {
    io.out := io.in_a + io.in_b
  }.otherwise{
    io.out := io.in_a - io.in_b
  }
}
