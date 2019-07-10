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

class RegisterPort extends Bundle {
  val rs1 = Input(UInt(4.W))
  val rs2 = Input(UInt(4.W))
  val rd = Input(UInt(4.W))
  val writeEnable = Input(Bool())
  val writeData = Input(UInt(32.W))

  val rs1Data = Output(UInt(32.W))
  val rs2Data = Output(UInt(32.W))
}

class Register extends Module{
  val io = IO(new RegisterPort)

  val Regx1 = RegInit(0.U(32.W))
  val Regx2 = RegInit(0.U(32.W))
  val MainReg = Mem(8, UInt(32.W))

  when(io.rs1(3) === 0.U){
    when(io.rs1(2,0) === 1.U) {
      io.rs1Data := Regx1
    }.elsewhen(io.rs1(2,0) === 2.U) {
      io.rs1Data := Regx2
    }.otherwise {
      io.rs1Data := 0.U
    }
  }.otherwise{
      io.rs1Data := MainReg(io.rs1(2,0))
  }

  when(io.rs2(3) === 0.U){
    when(io.rs2(2,0) === 1.U) {
      io.rs2Data := Regx1
    }.elsewhen(io.rs2(2,0) === 2.U) {
      io.rs2Data := Regx2
    }.otherwise {
      io.rs2Data := 0.U
    }
  }.otherwise{
    io.rs2Data := MainReg(io.rs2(2,0))
  }

  when(io.writeEnable === true.B) {
    when(io.rd(3) === 1.U) {
      MainReg(io.rd(2, 0)) := io.writeData
    }.otherwise {
      when(io.rd(2, 0) === 1.U) {
        Regx1 := io.writeData
      }.elsewhen(io.rd(2, 0) === 2.U) {
        Regx2 := io.writeData
      }.otherwise {
      }
    }
  }.otherwise{}
}
