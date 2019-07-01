package Memory

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}



class InstRomSpec extends ChiselFlatSpec {
  "Distortion" should "parametric full test" in {
    assert(Driver(() => new InstRom) {
      c =>
        new PeekPokeTester(c) {
          for (i <- 0 until 16) {
            val test_i = i.asUInt(4.W)
            poke(c.io.readAddress, test_i)
            expect(c.io.out, test_i)
          }
        }
    })
    true
  }

}
