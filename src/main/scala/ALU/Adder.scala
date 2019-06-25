package ALU

import chisel3._
import chisel3.util._

class AdderPort extends Bundle {
  val in_a = Input(SInt(32.W))
  val in_b = Input(SInt(32.W))
  val out = Output(SInt(32.W))
}

class Adder extends Module {
  val io = IO(new AdderPort)

  io.out := io.in_a + io.in_b
}
