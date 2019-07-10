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

class RegisterSpec extends ChiselFlatSpec {
  val source = Source.fromFile("src/test/binary/Decoder/Imm.bin")
  val lines = source.getLines


  "Distortion" should "parametric full test" in {
    assert(Driver(() => new Register) {
      c =>
        new PeekPokeTester(c) {
          lines.foreach{ s =>
            val inst = Integer.parseUnsignedInt(s.split(" ", 0)(1), 16).toLong
            val instUInt = inst.asUInt(16.W)
            poke(c.io.inst, instUInt)
            if(Instructions.isLWSP(inst)) {
              println("LWSP")
              val (rd, rs1, rs2) = Instructions.regLWSP(inst)
              expect(c.io.rd, rd.U)
              expect(c.io.rs1, rs1.U)
            }else if(Instructions.isSWSP(inst)){
              println("SWSP")
              val (rd, rs1, rs2) = Instructions.regSWSP(inst)
              expect(c.io.rd, rd.U)
              expect(c.io.rs1, rs1.U)
            }else if(Instructions.isLW(inst)){
              println("LW")
              val (rd, rs1, rs2) = Instructions.regLW(inst)
              expect(c.io.rd, rd.U)
              expect(c.io.rs1, rs1.U)
            }else if(Instructions.isSW(inst)){
              println("SW")
              val (rd, rs1, rs2) = Instructions.regSW(inst)
              expect(c.io.rd, rd.U)
              expect(c.io.rs1, rs1.U)
            }else if(Instructions.isJ(inst)){
              println("J")
              val (rd, rs1, rs2) = Instructions.regJ(inst)
              expect(c.io.rd, rd.U)
            }else if(Instructions.isJAL(inst)){
              println("JAL")
              val (rd, rs1, rs2) = Instructions.regJAL(inst)
              expect(c.io.rd, rd.U)
            }else if(Instructions.isJR(inst)){
              println("JR")
              val (rd, rs1, rs2) = Instructions.regJR(inst)
              expect(c.io.rd, rd.U)
            }else if(Instructions.isJALR(inst)){
              println("JARL")
              val (rd, rs1, rs2) = Instructions.regJALR(inst)
              expect(c.io.rd, rd.U)
            }else if(Instructions.isBEQZ(inst)){
              println("BEQZ")
              val (rd, rs1, rs2) = Instructions.regBEQZ(inst)
              expect(c.io.rd, rd.U)
              expect(c.io.rs1, rs1.U)
            }else if(Instructions.isBNEZ(inst)){
              println("BNEZ")
              val (rd, rs1, rs2) = Instructions.regBNEZ(inst)
              expect(c.io.rd, rd.U)
              expect(c.io.rs1, rs1.U)
            }else if(Instructions.isLI(inst)){
              println("LI")
              val (rd, rs1, rs2) = Instructions.regLI(inst)
              expect(c.io.rd, rd.U)
              expect(c.io.rs1, rs1.U)
            }else if(Instructions.isMV(inst)){
              println("MV")
              val (rd, rs1, rs2) = Instructions.regMV(inst)
              expect(c.io.rd, rd.U)
              expect(c.io.rs1, rs1.U)
              expect(c.io.rs2, rs2.U)
            }else if(Instructions.isADD(inst)){
              println("ADD")
              val (rd, rs1, rs2) = Instructions.regADD(inst)
              expect(c.io.rd, rd.U)
              expect(c.io.rs1, rs1.U)
              expect(c.io.rs2, rs2.U)
            }else if(Instructions.isSUB(inst)){
              println("SUB")
              val (rd, rs1, rs2) = Instructions.regSUB(inst)
              expect(c.io.rd, rd.U)
              expect(c.io.rs1, rs1.U)
              expect(c.io.rs2, rs2.U)
            }else if(Instructions.isNOP(inst)){
              println("NOP")
            }else{
              println("Others")
              false
            }
          }
        }
    })
    true
  }
}
