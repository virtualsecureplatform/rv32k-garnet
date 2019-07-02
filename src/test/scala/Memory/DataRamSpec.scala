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

class DataRamSpec extends ChiselFlatSpec{
  "Distortion" should "parametric full test" in {
    assert(Driver(() => new DataRam) {
      c =>
        new PeekPokeTester(c) {
          var testDataArray: Array[UInt] = Array.empty
          for(i <- 0 until 1024){
            val v:BigInt = Random.nextLong() & 0xffffffffL
            val test_i = v.asUInt(32.W)
            testDataArray = testDataArray :+ test_i
            poke(c.io.writeData, test_i)
            poke(c.io.writeAddress, i.asUInt(10.W))
            poke(c.io.writeEnable, true)
            step(1)
          }
          poke(c.io.writeEnable, false)
          for(i <- 0 until 1024){
            val v:BigInt = Random.nextLong() & 0xffffffffL
            val test_i = v.asUInt(32.W)
            poke(c.io.writeData, test_i)
            poke(c.io.writeAddress, i.asUInt(10.W))
            step(1)
          }
          for(i <- 0 until 1024){
            poke(c.io.readAddress, i.asUInt(10.W))
            expect(c.io.out, testDataArray(i))
          }
        }
    })
  }

}
