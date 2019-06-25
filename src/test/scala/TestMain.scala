import ALU.{Adder, AdderTester}
import chisel3.iotesters.Driver

object TestMain extends App {
  assert(Driver(() => new Adder) {c => new AdderTester(c)})
  println("Success")
}