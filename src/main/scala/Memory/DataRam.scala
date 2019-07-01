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
