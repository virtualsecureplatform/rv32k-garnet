package Decoder

import Config.Instructions
import chisel3._
import chisel3.util.Cat

class ALUPort extends Bundle {
  val inst = Input(UInt(16.W))
  val opcode = Output(UInt(1.W))
}

class ALU extends Module {
  val io = IO(new ALUPort)

  when(io.inst === Instructions.LWSP) {
    io.opcode := 0.U
  }.elsewhen(io.inst === Instructions.SWSP) {
    io.opcode := 0.U
  }.elsewhen(io.inst === Instructions.LW || io.inst === Instructions.SW) {
    io.opcode := 0.U
  }.elsewhen(io.inst === Instructions.J) {
    io.opcode := 0.U
  }.elsewhen(io.inst === Instructions.JAL) {
    io.opcode := 0.U
  }.elsewhen(io.inst === Instructions.JR) {
    io.opcode := 0.U
  }.elsewhen(io.inst === Instructions.JALR) {
    io.opcode := 0.U
  }.elsewhen(io.inst === Instructions.BEQZ || io.inst === Instructions.BNEZ) {
    io.opcode := 0.U
  }.elsewhen(io.inst === Instructions.LI) {
    io.opcode := 0.U
  }.elsewhen(io.inst === Instructions.MV){
    io.opcode := 0.U
  }.elsewhen(io.inst === Instructions.ADD){
    io.opcode := 0.U
  }.elsewhen(io.inst === Instructions.SUB){
    io.opcode := 1.U
  }.otherwise{
    io.opcode := 0.U
  }


}
