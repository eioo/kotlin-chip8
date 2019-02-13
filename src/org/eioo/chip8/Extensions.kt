package org.eioo.chip8

// Tyvm! https://github.com/mvukic/chip8emulator/blob/master/src/main/kotlin/hr/mvukic/chip8emu/Extensions.kt

fun Int.toHex() = Integer.toHexString(this)!!
fun Byte.toHex() = Integer.toHexString(this.toInt())!!

fun Byte.high() = (this.toInt() and 0xf0) shr 4
fun Byte.low() = this.toInt() and 0xf

fun Byte.toPositiveInt() = toInt() and 0xFF
