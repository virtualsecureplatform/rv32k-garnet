package Decoder

import Config.Instructions
import chisel3._
import chisel3.util.Cat

class RegisterPort extends Bundle {
  val inst = Input(UInt(16.W))
  val rd = Output(UInt(5.W))
  val rs1 = Output(UInt(5.W))
  val rs2 = Output(UInt(5.W))
}

class Register extends Module {
  val io = IO(new RegisterPort)

  when(io.inst === Instructions.LWSP) {
    io.rd := io.inst(11, 7)
    io.rs1 := 0x2.U(5.W)
    io.rs2 := DontCare
  }.elsewhen(io.inst === Instructions.SWSP) {
    io.rd := io.inst(6, 2)
    io.rs1 := 0x2.U(5.W)
    io.rs2 := DontCare
  }.elsewhen(io.inst === Instructions.LW || io.inst === Instructions.SW) {
    io.rd := Cat(1.U(2.W), io.inst(4, 2))
    io.rs1 := Cat(1.U(2.W), io.inst(9, 7))
    io.rs2 := DontCare
  }.elsewhen(io.inst === Instructions.J) {
    io.rd := 0.U(5.W)
    io.rs1 := DontCare
    io.rs2 := DontCare
  }.elsewhen(io.inst === Instructions.JAL) {
    io.rd := 1.U(5.W)
    io.rs1 := DontCare
    io.rs2 := DontCare
  }.elsewhen(io.inst === Instructions.JR) {
    io.rd := 0.U(5.W)
    io.rs1 := io.inst(11, 7)
    io.rs2 := DontCare
  }.elsewhen(io.inst === Instructions.JALR) {
    io.rd := 1.U(5.W)
    io.rs1 := io.inst(11, 7)
    io.rs2 := DontCare
  }.elsewhen(io.inst === Instructions.BEQZ || io.inst === Instructions.BNEZ) {
    io.rd := 0.U(5.W)
    io.rs1 := Cat(1.U(2.W), io.inst(9, 7))
    io.rs2 := DontCare
  }.elsewhen(io.inst === Instructions.LI) {
    io.rd := io.inst(11, 7)
    io.rs1 := 0.U(5.W)
    io.rs2 := DontCare
  }.elsewhen(io.inst === Instructions.MV){
    io.rd := io.inst(11, 7)
    io.rs1 := 0.U(5.W)
    io.rs2 := io.inst(6, 2)
  }.elsewhen(io.inst === Instructions.ADD){
    io.rd := io.inst(11, 7)
    io.rs1 := io.inst(11, 7)
    io.rs2 := io.inst(6, 2)
  }.elsewhen(io.inst === Instructions.SUB){
    io.rd := Cat(1.U(2.W), io.inst(9, 7))
    io.rs1 := Cat(1.U(2.W), io.inst(9, 7))
    io.rs2 := Cat(1.U(2.W), io.inst(4, 2))
  }.otherwise{
    io.rd := DontCare
    io.rs1 := DontCare
    io.rs2 := DontCare
  }


}
