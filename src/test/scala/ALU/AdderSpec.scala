package ALU

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

import scala.util.Random


class AdderSpec extends ChiselFlatSpec {
  "Distortion" should "parametric full test" in {
    assert(Driver(() => new Adder) {
      c =>
        new PeekPokeTester(c) {
          for (i <- 0 until 100) {
            val in_a = Random.nextInt()
            val in_b = Random.nextInt()
            poke(c.io.in_a, in_a)
            poke(c.io.in_b, in_b)
            expect(c.io.out, in_a)
          }
        }
    })
    true
  }
}
