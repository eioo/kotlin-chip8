package org.eioo.chip8

fun main() {
    val emu = Emulator()
    emu.loadRom("roms/pong.rom")
    emu.start()
}