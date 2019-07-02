package Controller

import chisel3._
import chisel3.util._

class StateMachinePort extends Bundle {
  val clockIF = Output(Bool())
  val clockID = Output(Bool())
  val clockEX = Output(Bool())
  val clockMEM = Output(Bool())
  val clockWB = Output(Bool())

}
class StateMachine extends Module{
  val io = IO(new StateMachinePort)

  val state = RegInit(1.U(5.W))
  state := Cat(state, state(4))

  io.clockIF := state(0)
  io.clockID := state(1)
  io.clockEX := state(2)
  io.clockMEM := state(3)
  io.clockWB := state(4)

}
