package org.eioo.chip8

fun main() {
    val emu = Emulator()
    emu.start()
    emu.loadRom("roms/pong.rom")

    while (emu.running) {
        emu.emulateCycle()

        if (emu.drawFlag) {
            println("Drawing gfx")
            emu.drawFlag = false  // TODO: Implement
        }

        emu.setKeys()
    }
}