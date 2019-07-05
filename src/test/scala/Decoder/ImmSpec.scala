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

package Decoder

import Config.Instructions
import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

import scala.io.Source

class ImmSpec extends ChiselFlatSpec {
  val source = Source.fromFile("src/test/binary/Decoder/Imm.bin")
  val lines = source.getLines


  "Distortion" should "parametric full test" in {
    assert(Driver(() => new Imm) {
      c =>
        new PeekPokeTester(c) {
          lines.foreach{ s =>
            val inst = Integer.parseUnsignedInt(s.split(" ", 0)(1), 16).toLong
            val instUInt = inst.asUInt(16.W)
            poke(c.io.inst, instUInt)
            if(Instructions.isLI(inst)){
              println("LI")
              expect(c.io.out, Instructions.immLI(inst).asUInt(6.W))
            }else{
              println("Others")
              expect(c.io.out, 0.U)
            }
          }
        }
    })
    true
  }
}
