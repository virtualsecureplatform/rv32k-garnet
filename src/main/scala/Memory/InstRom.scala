package Memory

import chisel3._
import chisel3.util._

class InstRomPort extends Bundle {
  val readAddress = Input(UInt(4.W))

  val out = Output(UInt(32.W))

}
class InstRom extends Module{
  val io = IO(new InstRomPort);

  def romData()={
    val times = (0 until 16).map(i => i.asUInt(32.W))
    VecInit(times)
  }

  io.out := romData()(io.readAddress)
}
