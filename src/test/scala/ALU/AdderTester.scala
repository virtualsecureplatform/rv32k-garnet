package ALU

import chisel3.iotesters.PeekPokeTester

import scala.util.Random

class AdderTester(c: Adder) extends PeekPokeTester(c) {
  for(i <- 0 until 100) {
    val in_a = Random.nextInt(32)
    val in_b = Random.nextInt(32)
    poke(c.io.in_a, in_a)
    poke(c.io.in_b, in_b)
    expect(c.io.out, in_a+in_b)
  }
}
