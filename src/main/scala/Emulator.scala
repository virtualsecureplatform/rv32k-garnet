import Config.Instructions

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

class Emulator {

  val ifRom = new Utils.TestRom
  var ifPC = -1

  var idRegister: Array[Long] = new Array[Long](16)
  var st = 0
  var inst:Long = 0
  var regs = (0, 0, 0)
  var regData:(Long, Long) = (0, 0)
  var immData:(Boolean, Long) = (false, 0)
  var selImm:Boolean = false
  var opcodeALU = 0
  var execRes:Long = 0

  def step = {
    if(st == 0) {
      ifPC = updatePC(ifPC, false, 0)
      inst = fetchInst(ifPC)
      st = 1
    }else if(st == 1) {
      regs = decodeRegister(inst)
      regData = readRegister(regs._2, regs._3)
      immData = decodeImm(inst)
      selImm = decodeImmSelector(immData._1, false, false)
      opcodeALU = decodeALU(inst)
      st = 2
    }else if(st == 2) {
      execRes = execALU(regData._1, regData._2, immData._2, selImm, opcodeALU)
      st = 3
    }else if(st == 3) {
      writeMemory()
      st = 4
    }else if(st == 4) {
      writeRegister(regs._1, execRes)
      showRegisters
      st = 0
    }
  }

  def showRegisters = {
    println("\n--------- Register Information ---------")
    println(" x1: %08X  x2: %08X".format(idRegister(1), idRegister(2)))
    println(" x8: %08X  x9: %08X x10: %08X x11: %08X".format(idRegister(8), idRegister(9), idRegister(10), idRegister(11)))
    println("x12: %08X x13: %08X x14: %08X x15: %08X\n".format(idRegister(12), idRegister(13), idRegister(14), idRegister(15)))
  }
  private def updatePC (pc:Int, jump:Boolean, jumpOffset:Int):Int = {
    var pcUpdated = 0
    if(jump){
      pcUpdated = pc+jumpOffset
    }else{
      pcUpdated = pc+1
    }
    println("[IF] Update PC: %04X".format(pcUpdated))
    pcUpdated
  }

  private def fetchInst(pc:Int):Long = {
    val inst = ifRom.fetch(pc)
    println("[IF] Fetch Instruction: %04X".format(inst))
    inst
  }

  private def decodeRegister(inst:Long):(Int, Int, Int) = {
    var regs = (0, 0, 0)
    print("[ID] ")
    if(Instructions.isLWSP(inst)){
      println("Decode Instruction: LWSP")
      regs = Instructions.regLWSP(inst)
    }else if(Instructions.isSWSP(inst)) {
      println("Decode Instruction: SWSP")
      regs = Instructions.regSWSP(inst)
    }else if(Instructions.isLW(inst)) {
      println("Decode Instruction: LW")
      regs = Instructions.regLW(inst)
    }else if(Instructions.isSW(inst)) {
      println("Decode Instruction: SW")
      regs = Instructions.regSW(inst)
    }else if(Instructions.isJ(inst)) {
      println("Decode Instruction: J")
      regs = Instructions.regJ(inst)
    }else if(Instructions.isJAL(inst)) {
      println("Decode Instruction: JAL")
      regs = Instructions.regJAL(inst)
    }else if(Instructions.isJR(inst)) {
      println("Decode Instruction: JR")
      regs = Instructions.regJR(inst)
    }else if(Instructions.isJALR(inst)) {
      println("Decode Instruction: JALR")
      regs = Instructions.regJALR(inst)
    }else if(Instructions.isBEQZ(inst)) {
      println("Decode Instruction: BEQZ")
      regs = Instructions.regBEQZ(inst)
    }else if(Instructions.isBNEZ(inst)) {
      println("Decode Instruction: BNEZ")
      regs = Instructions.regBNEZ(inst)
    }else if(Instructions.isLI(inst)) {
      println("Decode Instruction: LI")
      regs = Instructions.regLI(inst)
    }else if(Instructions.isMV(inst)) {
      println("Decode Instruction: MV")
      regs = Instructions.regMV(inst)
    }else if(Instructions.isADD(inst)) {
      println("Decode Instruction: ADD")
      regs = Instructions.regADD(inst)
    }else if(Instructions.isSUB(inst)) {
      println("Decode Instruction: SUB")
      regs = Instructions.regSUB(inst)
    }else if(Instructions.isNOP(inst)) {
      println("Decode Instruction: NOP")
      regs = Instructions.regNOP(inst)
    }else{
      println("Decode Instruction: Unexpected Instruction %04X".format(inst))
    }
    println("[ID] Decode Register: rd:%d rs1:%d rs2:%d".format(regs._1, regs._2, regs._3))
    regs
  }

  private def decodeImm(inst:Long): (Boolean, Long) ={
    if(Instructions.isLI(inst)){
      val v = Instructions.immLI(inst)
      println("[ID] Decode Immediate: %08X".format(v))
      (true, v)
    }else{
      (false, 0)
    }
  }

  private def decodeALU(inst:Long): Int = {
    var opcode = 0
    if(Instructions.isSUB(inst)){
      opcode = 1
    }
    println("[ID] Decode ALU Opcode: %01X".format(opcode))
    opcode
  }

  private def readRegister(rs1:Int, rs2:Int): (Long, Long) = {
    val rs1Data = idRegister(rs1)
    val rs2Data = idRegister(rs2)
    println("[ID] ReadRegister: rs1:%08X rs2:%08X".format(rs1Data, rs2Data))
    (rs1Data, rs2Data)
  }

  private def decodeImmSelector(imm:Boolean, MemRead:Boolean, MemWrite:Boolean): Boolean = {
    val sel = imm || MemRead || MemWrite
    println("[ID] Decode Imm Selector: %s".format(sel.toString()))
    sel
  }

  private def execALU(rs1Data:Long, rs2Data:Long, immData:Long, immSel:Boolean, opcode:Int): Long = {
    val inA = rs1Data
    val inB = if(immSel) immData else rs2Data
    if(opcode == 0){
      val res = inA + inB
      println("[EX] Exec ALU %08X + %08X = %08X".format(inA, inB, res))
      res
    }else{
      val res = inA - inB
      println("[EX] Exec ALU %08X - %08X = %08X".format(inA, inB, res))
      res
    }
  }

  private def writeMemory() = println("[MEM] Not Implemented")
  private def readMemory() = println("[MEM] Not Implemented")

  private def writeRegister(rd:Int, data:Long) = {
    idRegister(rd) = data
    println("[WB] Write Register: rd:%d value:%08X".format(rd, data))
  }
}
