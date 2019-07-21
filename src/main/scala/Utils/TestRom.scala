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

package Utils

import scala.io.Source
import chisel3._

class TestRom {
  val source = Source.fromFile("src/test/binary/Decoder/MemoryRead.bin")
  val lines = source.getLines.toList

  def fetch(addr:Int):Long = {
    val instStr = lines(addr)
    val inst = Integer.parseUnsignedInt(instStr.split(" ", 0)(1), 16).toLong
    return inst
  }

  def length:Int = lines.length
}
