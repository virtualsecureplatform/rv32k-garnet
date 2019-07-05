package Decoder


import Config.Instructions
import chisel3._
import chisel3.util.Cat

class ImmPort extends Bundle {
  val inst = Input(UInt(16.W))
  val out = Output(UInt(6.W))
}

class Imm extends Module{
  val io = IO(new ImmPort)

  when(io.inst === Instructions.LI) {
    io.out := Cat(io.inst(12), io.inst(6, 2))
  }.otherwise{
    io.out := 0x0.U(6.W)
  }
}
