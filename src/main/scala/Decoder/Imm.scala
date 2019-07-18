package Decoder


import Config.Instructions
import chisel3._
import chisel3.util.Cat

class ImmPort extends Bundle {
  val inst = Input(UInt(16.W))
  val out = Output(UInt(6.W))
  val sel = Output(Bool())
}

class Imm extends Module{
  val io = IO(new ImmPort)

  when(io.inst === Instructions.LI) {
    io.out := Cat(io.inst(12), io.inst(6, 2))
    io.sel := true.B
  }.otherwise{
    io.out := 0x0.U(6.W)
    io.sel := false.B
  }
}
