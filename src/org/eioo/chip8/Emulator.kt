package org.eioo.chip8

class Emulator {
    private var delayTimer: UShort? = null            // 60Hz counts to zero
    private var soundTimer: UShort? = null            // -//-

    private var opcode: UShort? = null                // Current operation code
    private val memory: UIntArray = UIntArray(4096)   // Memory
    private val gfx: UIntArray = UIntArray(64 * 32)   // Graphics, 1 bit for each pixelMemory
    private val key: UIntArray = UIntArray(16)        // Pressed keys
    private val V: UShortArray = UShortArray(16)      // Registers (V0, V1, ... VE) VE: Carry flag
    private val stack: UShortArray = UShortArray(16)  // Stack
    private var sp: UInt? = null                      // Stack pointer
    private var I: UShort? = null                     // Index register
    private var pc: UShort? = null                    // Program counter (0x000 -> 0xFFF)

    var running: Boolean = false                      // State of machine
    var drawFlag: Boolean = false

    init {
        setupGraphics()
        setupKeys()
    }

    fun start() {
        running = true
    }

    fun stop() {
        running = false
    }

    private fun setupGraphics() {
        for (i in 0..gfx.size) {
            gfx[i] = 0u
        }
    }

    private fun setupKeys() {
        for (i in 0..key.size) {
            key[i] = 0u
        }
    }

    fun setKeys() {
        // TODO: Check for key presses/releases, preferably in another file
    }

    fun loadRom(romPath: String) {
        println("Loading ROM from path: $romPath")
        // TODO: Load rom
    }

    fun emulateCycle() {
        // TODO: Emulate cycle
    }
}