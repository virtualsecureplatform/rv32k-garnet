import ALU.Adder
import Memory.{DataRam, InstRom}
import chisel3.iotesters.Driver

object Main extends App {
  chisel3.Driver.execute(args, ()=>new DataRam())
}