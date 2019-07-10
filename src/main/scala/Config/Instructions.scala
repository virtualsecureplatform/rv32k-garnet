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

package Config

import chisel3.util.BitPat

object Instructions {
  def LWSP   = BitPat("b010???????????10")
  def SWSP   = BitPat("b110???????????10")

  def LW     = BitPat("b010???????????00")
  def SW     = BitPat("b110???????????00")

  def J      = BitPat("b101???????????01")
  def JAL    = BitPat("b001???????????01")

  def JR     = BitPat("b1000?????0000010")
  def JALR   = BitPat("b1001?????0000010")

  def BEQZ   = BitPat("b110???????????01")
  def BNEZ   = BitPat("b111???????????01")

  def LI     = BitPat("b010???????????01")

  def MV     = BitPat("b1000??????????10")
  def ADD    = BitPat("b1001??????????10")
  def SUB    = BitPat("b100011???00???01")

  def NOP = BitPat.bitPatToUInt(BitPat("b0000000000000001"))

  def isLWSP(inst:Long): Boolean =
    (((inst>>>13)&0x7) == 0x2) && ((inst&0x3) == 0x2)

  def isSWSP(inst:Long): Boolean =
    (((inst>>>13)&0x7) == 0x6) && ((inst&0x3) == 0x2)

  def isLW(inst:Long): Boolean =
    (((inst>>>13)&0x7) == 0x2) && ((inst&0x3) == 0x0)

  def isSW(inst:Long): Boolean =
    (((inst>>>13)&0x7) == 0x6) && ((inst&0x3) == 0x0)

  def isJ(inst:Long): Boolean =
    (((inst>>13)&0x7) == 0x5) && ((inst&0x3) == 0x1)

  def isJAL(inst:Long): Boolean =
    (((inst>>13)&0x7) == 0x1) && ((inst&0x3) == 0x1)

  def isJR(inst:Long): Boolean =
    (((inst>>12)&0xF) == 0x8) && ((inst&0x7F) == 0x2)

  def isJALR(inst:Long): Boolean =
    (((inst>>12)&0xF) == 0x9) && ((inst&0x7F) == 0x2)

  def isBEQZ(inst:Long): Boolean =
    (((inst>>13)&0x7) == 0x6) && ((inst&0x3) == 0x2)

  def isBNEZ(inst:Long): Boolean =
    (((inst>>13)&0x7) == 0x7) && ((inst&0x3) == 0x2)

  def isLI(inst:Long): Boolean =
    (((inst>>13)&0x7) == 0x2) && ((inst&0x3) == 0x1)

  def isMV(inst:Long): Boolean =
    (((inst>>12)&0xF) == 0x8) && ((inst&0x3) == 0x2)

  def isADD(inst:Long): Boolean =
    (((inst>>12)&0xF) == 0x9) && ((inst&0x3) == 0x2)

  def isSUB(inst:Long): Boolean =
    (((inst>>10)&0x3F) == 0x23) && (((inst>>5)&0x3) == 0x0) && ((inst&0x3) == 0x1)

  def isNOP(inst:Long): Boolean =
    (inst&0xFF) == 0x1

  def regLWSP(inst:Long): (Int,Int,Int) = (((inst>>7)&0x1F).toInt, 0x2, 0)

  def regSWSP(inst:Long): (Int,Int,Int) = (((inst>>2)&0x1F).toInt, 0x2, 0)

  def regLW(inst:Long): (Int,Int,Int) = (((inst>>2)&0x7).toInt+8, ((inst>>7)&0x7).toInt+8, 0)

  def regSW(inst:Long): (Int,Int,Int) = (((inst>>2)&0x7).toInt+8, ((inst>>7)&0x7).toInt+8, 0)

  def regJ(inst:Long): (Int,Int,Int) = (0x0, 0, 0)

  def regJAL(inst:Long): (Int,Int,Int) = (0x1, 0, 0)

  def regJR(inst:Long): (Int,Int,Int) = (0x0, ((inst>>7)&0x1F).toInt, 0)

  def regJALR(inst:Long): (Int,Int,Int) = (0x1, ((inst>>7)&0x1F).toInt, 0)

  def regBEQZ(inst:Long): (Int,Int,Int) = (0, ((inst>>7)&0x7).toInt+8, 0x0)

  def regBNEZ(inst:Long): (Int,Int,Int) = (0, ((inst>>7)&0x7).toInt+8, 0x0)

  def regLI(inst:Long): (Int,Int,Int) = (((inst>>7)&0x1F).toInt, 0x0, 0)

  def regMV(inst:Long): (Int,Int,Int) = (((inst>>7)&0x1F).toInt, 0x0, ((inst>>2)&0x1F).toInt)

  def regADD(inst:Long): (Int,Int,Int) = (((inst>>7)&0x1F).toInt, ((inst>>7)&0x1F).toInt, ((inst>>2)&0x1F).toInt)

  def regSUB(inst:Long): (Int,Int,Int) = (((inst>>7)&0x7).toInt+8, ((inst>>7)&0x7).toInt+8, ((inst>>2)&0x7).toInt+8)

  def regNOP(inst:Long): (Int,Int,Int) = (0, 0, 0)

  def immLWSP(inst:Long): Long =
    (((inst>>>12)&0x1)<<5)+(((inst>>>4)&0x7)<<2)+(((inst>>>2)&0x3)<<6)

  def immSWSP(inst:Long): Long =
    (((inst>>>7)&0x3)<<6)+(((inst>>>9)&0xF)<< 2)

  def immLW(inst:Long): Long =
    (((inst>>>5)&0x1)<<6)+(((inst>>>10)&0x7)<<3)+(((inst>>>6)&0x1)<<2)

  def immSW(inst:Long): Long =
    (((inst>>>5)&0x1)<<6)+(((inst>>>10)&0x7)<<3)+(((inst>>>6)&0x1)<<2)

  def immLI(inst:Long): Long =
    (((inst>>12)&0x1)<<5)+((inst>>2)&0x1F)

}

