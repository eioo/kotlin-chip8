package org.eioo.chip8

fun main() {
    val emu = Emulator()
    emu.start()

    while (emu.running) {
        emu.emulateCycle()
        emu.loadRom("")

        if (emu.drawFlag) {
            println("Drawing gfx")
            emu.drawFlag = false  // TODO: Implement
        }

        emu.setKeys()
    }
}