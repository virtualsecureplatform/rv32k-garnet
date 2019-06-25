import ALU.Adder
import chisel3.iotesters.Driver

object Main extends App {
  chisel3.Driver.execute(args, ()=>new Adder())
}