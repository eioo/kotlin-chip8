package com.eioo.chip8

class Memory(private var emu: Emulator) {
    private val resUtils: ResourceUtils = ResourceUtils()
    private val memory: ByteArray = ByteArray(4096)

    private val fontset = intArrayOf(
        0xF0, 0x90, 0x90, 0x90, 0xF0, // 0
        0x20, 0x60, 0x20, 0x20, 0x70, // 1
        0xF0, 0x10, 0xF0, 0x80, 0xF0, // 2
        0xF0, 0x10, 0xF0, 0x10, 0xF0, // 3
        0x90, 0x90, 0xF0, 0x10, 0x10, // 4
        0xF0, 0x80, 0xF0, 0x10, 0xF0, // 5
        0xF0, 0x80, 0xF0, 0x90, 0xF0, // 6
        0xF0, 0x10, 0x20, 0x40, 0x40, // 7
        0xF0, 0x90, 0xF0, 0x90, 0xF0, // 8
        0xF0, 0x90, 0xF0, 0x10, 0xF0, // 9
        0xF0, 0x90, 0xF0, 0x90, 0x90, // A
        0xE0, 0x90, 0xE0, 0x90, 0xE0, // B
        0xF0, 0x80, 0x80, 0x80, 0xF0, // C
        0xE0, 0x90, 0x90, 0x90, 0xE0, // D
        0xF0, 0x80, 0xF0, 0x80, 0xF0, // E
        0xF0, 0x80, 0xF0, 0x80, 0x80  // F
    )

    fun read(addr: Int) = memory[addr]
    fun write(addr: Int, value: Byte) = memory.set(addr, value)
    fun write(addr: Int, value: Int) = memory.set(addr, value.toByte())

    fun reset() {
        for (i in 0 until memory.size) memory[i] = 0
        loadFontset()
    }

    private fun loadFontset() {
        for (i in 0..79) memory[i] = fontset[i].toByte()
    }

    fun loadRom(romPath: String) {
        println("Loading ROM from path: \"$romPath\"")
        val res = resUtils.getResource(romPath)

        if (res == null) {
            println("Rom not found")
            return emu.stop()
        }

        val fileBytes = res.readBytes()

        for (i in 0 until fileBytes.size) {
            write(0x200 + i, fileBytes[i])
        }

        println("ROM loaded, byte size: ${fileBytes.size}")
    }
}