package Controller

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import scala.util.Random

class StateMachineSpec extends  ChiselFlatSpec {
  "Distortion" should "parametric full test" in {
    assert(Driver(() => new StateMachine) {
      c =>
        new PeekPokeTester(c) {
          for (i <- 0 until 100) {
            i%5 match {
              case 0 =>
                expect(c.io.clockIF, true)
                expect(c.io.clockID, false)
                expect(c.io.clockEX, false)
                expect(c.io.clockMEM, false)
                expect(c.io.clockWB, false)
              case 1 =>
                expect(c.io.clockIF, false)
                expect(c.io.clockID, true)
                expect(c.io.clockEX, false)
                expect(c.io.clockMEM, false)
                expect(c.io.clockWB, false)
              case 2 =>
                expect(c.io.clockIF, false)
                expect(c.io.clockID, false)
                expect(c.io.clockEX, true)
                expect(c.io.clockMEM, false)
                expect(c.io.clockWB, false)
              case 3 =>
                expect(c.io.clockIF, false)
                expect(c.io.clockID, false)
                expect(c.io.clockEX, false)
                expect(c.io.clockMEM, true)
                expect(c.io.clockWB, false)
              case 4 =>
                expect(c.io.clockIF, false)
                expect(c.io.clockID, false)
                expect(c.io.clockEX, false)
                expect(c.io.clockMEM, false)
                expect(c.io.clockWB, true)
            }
            step(1)
          }
        }
    })
    true
  }

}
