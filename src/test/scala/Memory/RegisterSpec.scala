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

class RegisterSpec extends ChiselFlatSpec{
  "Distortion" should "parametric full test" in {
    assert(Driver(() => new Register) {
      c =>
        new PeekPokeTester(c) {
          var testDataArray: Array[UInt] = Array.empty
          poke(c.io.rs1, 0.U)
          poke(c.io.rs2, 0.U)
          for(i <- 0 until 16){
            val v:BigInt = Random.nextLong() & 0xffffffffL
            val test_i = v.asUInt(32.W)
            testDataArray = testDataArray :+ test_i
            poke(c.io.writeEnable, true.B)
            poke(c.io.rd, i.U(4.W))
            poke(c.io.writeData, test_i)
            step(1)
          }
          for(i <- 0 until 16){
            val v:BigInt = Random.nextLong() & 0xffffffffL
            val test_i = v.asUInt(32.W)
            poke(c.io.writeEnable, false.B)
            poke(c.io.rd, i.U(4.W))
            poke(c.io.writeData, test_i)
            step(1)
          }
          for(i <- 0 until 16){
            poke(c.io.rs1, i.U(4.W))
            if((i>0 && i<3) || i>7){
              expect(c.io.rs1Data, testDataArray(i))
            }else{
              expect(c.io.rs1Data, 0.U(32.W))
            }
          }
          for(i <- 0 until 16){
            poke(c.io.rs2, i.U(4.W))
            if((i>0 && i<3) || i>7){
              expect(c.io.rs2Data, testDataArray(i))
            }else{
              expect(c.io.rs2Data, 0.U(32.W))
            }
          }
        }
    })
  }

}
