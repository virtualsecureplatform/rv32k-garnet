package ALU

import chisel3._
import chisel3.util._

class AdderPort extends Bundle {
  val in_a = Input(UInt(32.W))
  val in_b = Input(UInt(32.W))
  val out = Output(UInt(32.W))
}

class Adder extends Module {
  val io = IO(new AdderPort)

  io.out := io.in_a + io.in_b
}
