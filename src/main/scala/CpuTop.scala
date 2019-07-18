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

import chisel3._

class CpuTopPort extends Bundle {
  val rs1Data = Output(UInt(32.W))
  val rs2Data = Output(UInt(32.W))
  val ImmData = Output(SInt(32.W))

  val debugPort = Output(UInt(32.W))
}

class CpuTop extends Module{
  val io = IO(new CpuTopPort)

  val st = Module(new Controller.StateMachine)
  val ifStage = Module(new Stage.If)
  ifStage.io.jumpAddress := 0.U
  ifStage.io.jumpAddress := false.B

  val idStage = Module(new Stage.Id)
  idStage.io.regWriteData := 0.U

  val exStage = Module(new Stage.Ex)
  val memStage = Module(new Stage.Mem)
  val wbStage = Module(new Stage.Wb)

  ifStage.clock := st.io.clockIF.asClock()
  idStage.clock := st.io.clockID.asClock()
  exStage.clock := st.io.clockEX.asClock()
  idStage.io.wbClock := st.io.clockWB.asClock()
  memStage.clock := st.io.clockMEM.asClock()
  wbStage.clock := st.io.clockWB.asClock()

  idStage.io.inst := ifStage.io.instOut
  exStage.io.rs1Data := idStage.io.rs1Data
  exStage.io.rs2Data := idStage.io.rs2Data
  exStage.io.immData := idStage.io.immData
  exStage.io.opcodeALU := idStage.io.opcodeALU
  exStage.io.selImm := idStage.io.selImm

  memStage.io.in := exStage.io.out
  wbStage.io.in := memStage.io.out
  idStage.io.regWriteData := wbStage.io.out

  io.rs1Data := idStage.io.rs1Data
  io.rs2Data := idStage.io.rs2Data
  io.ImmData := idStage.io.immData
  io.debugPort := idStage.io.debugPort
}
