/*
Copyright 2019 Naoki Matsumoto

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package ALU

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import chisel3._
import scala.util.Random


class AdderSpec extends ChiselFlatSpec {
  "Distortion" should "parametric full test" in {
    assert(Driver(() => new Adder) {
      c =>
        new PeekPokeTester(c) {
          for (i <- 0 until 100) {
            val a = Random.nextInt()
            val b = Random.nextInt()
            poke(c.io.sel, 0.U)
            poke(c.io.in_a, a.asSInt(32.W))
            poke(c.io.in_b, b.asSInt(32.W))
            expect(c.io.out, (a + b).asSInt(32.W))
          }
          for (i <- 0 until 100) {
            val a = Random.nextInt()
            val b = Random.nextInt()
            poke(c.io.sel, 1.U)
            poke(c.io.in_a, a.asSInt(32.W))
            poke(c.io.in_b, b.asSInt(32.W))
            expect(c.io.out, (a - b).asSInt(32.W))
          }
        }
    })
    true
  }
}
