package org.eioo.chip8

class Emulator {

    private val memory: UIntArray = UIntArray(4096) // Memory
    private val gfx: IntArray = IntArray(64 * 32)   // Graphics, 1 bit for each pixelMemory
    private val key: IntArray = IntArray(16)        // Pressed keys
    private val V: ShortArray = ShortArray(16)      // Registers (V0, V1, ... VE) VE: Carry flag
    private val stack: ShortArray = ShortArray(16)  // Stack
    private var opcode: Short = 0                   // Current operation code
    private var sp: Int = 0                         // Stack pointer
    private var I: Short = 0                        // Index register
    private var pc: Int = 0                         // Program counter (0x000 -> 0xFFF)
    private var delayTimer: Short = 0               // 60Hz counts to zero
    private var soundTimer: Short = 0               // -//-

    var running: Boolean = false                    // State of machine
    var drawFlag: Boolean = false

    init {
        reset()
    }

    fun start() {
        running = true
        println("Emulator started")
    }

    fun stop() {
        running = false
        println("Emulator stopped")
    }

    private fun reset() {
        pc = 0x200
        opcode = 0
        I = 0
        sp = 0
        delayTimer = 0
        soundTimer = 0

        for (i in 0..(memory.size - 1)) memory[i] = 0u
        for (i in 0..(V.size - 1)) V[i] = 0
        for (i in 0..(stack.size - 1)) stack[i] = 0
        for (i in 0..(gfx.size - 1)) gfx[i] = 0
        for (i in 0..(key.size - 1)) key[i] = 0

        loadFontset()
    }

    fun setKeys() {
        // TODO: Check for key presses/releases, preferably in another file
    }

    private fun loadFontset() {
        for (i in 0..79) memory[i] = FONTSET[i].toUInt()
    }

    fun loadRom(romPath: String) {
        println("Loading ROM from path: \"$romPath\"")
        val res = this::class.java.classLoader.getResource(romPath)

        if (res == null) {
            println("Rom not found")
            return stop()
        }

        val fileBytes = res.readBytes()

        for (i in 0..(fileBytes.size - 1)) {
            memory[0x200 + i] = fileBytes[i].toUInt()
        }

        println("ROM loaded, byte size: ${fileBytes.size}")
    }

    fun emulateCycle() {
        opcode = (memory[pc] shl 8 or memory[pc + 1]).toShort()

        // Decode Opcode
        // Execute Opcode

        // Update timers
    }
}