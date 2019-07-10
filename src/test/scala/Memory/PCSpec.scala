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

package Memory

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

import scala.util.Random

class PCSpec extends ChiselFlatSpec{
  "Distortion" should "parametric full test" in {
    assert(Driver(() => new PC) {
      c =>
        new PeekPokeTester(c) {
          val v:BigInt = Random.nextLong() & 0xffffffffL - 100
          val test_v = v.U(32.W)
          poke(c.io.jump, false.B)
          poke(c.io.jumpAddress, test_v)
          for(i <- 0 until 10){
            expect(c.io.pcOut, i.U(32.W))
            step(1)
          }
          poke(c.io.jump, true.B)
          step(1)
          for(i <- 0 until 10){
            expect(c.io.pcOut, test_v)
            step(1)
          }
          poke(c.io.jump, false.B)
          for(i <-0 until 10){
            val inc = v+i
            expect(c.io.pcOut, inc.U(32.W))
            step(1)
          }
        }
    })
  }

}
