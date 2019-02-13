package org.eioo.chip8

class Emulator {

    private val memory: ByteArray = ByteArray(4096) // Memory
    private val gfx: IntArray = IntArray(64 * 32)   // Graphics, 1 bit for each pixelMemory
    private val key: IntArray = IntArray(16)        // Pressed keys
    private val V: IntArray = IntArray(16)          // Registers (V0, V1, ... VE) VE: Carry flag
    private val stack: IntArray = IntArray(16)      // Stack
    private var opcode: Int = 0                     // Current operation code
    private var sp: Int = 0                         // Stack pointer
    private var I: Int = 0                          // Index register
    private var pc: Int = 0                         // Program counter (0x000 -> 0xFFF)
    private var delayTimer: Int = 0                 // 60Hz counts to zero
    private var soundTimer: Int = 0                 // -//-

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

        for (i in 0..(memory.size - 1)) memory[i] = 0
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
        for (i in 0..79) memory[i] = FONTSET[i].toByte()
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
            memory[0x200 + i] = fileBytes[i]
        }

        println("ROM loaded, byte size: ${fileBytes.size}")
    }

    fun emulateCycle() {
        val msb = memory[pc]
        val lsb = memory[pc + 1]

        opcode = (msb.toInt() shl 8 or lsb.toPositiveInt()).and(0xffff)
        print(pc.toHex().padEnd(10, ' ') + opcode.toHex().padEnd(10, ' '))

        when (msb.high()) {
            0x2 -> {
                // 2nnn - CALL addr
                // Call subroutine at nnn.
                val addr = opcode and 0xfff

                sp++            // TODO: May be the other way around
                stack[sp] = pc
                pc = addr

                println("CALL ${addr.toHex()}")
            }
            0x6 -> {
                // 6xkk - LD Vx, byte
                // Set Vx = kk.
                val x = msb.low()
                val byte = lsb.toPositiveInt()

                V[x] = byte
                pc += 2

                println("LD V${x.toHex()}, ${byte.toHex()}")
            }
            0xa -> {
                // Annn - LD I, addr
                // Set I = nnn.
                val addr = opcode and 0xfff

                I = addr
                pc += 2

                println("LD I, ${addr.toHex()}")
            }
            0xd -> {
                // Dxyn - DRW Vx, Vy, nibble
                // Display n-byte sprite starting at memory location I at (Vx, Vy), set VF = collision.
                val x = msb.high()
                val y = msb.low()
                val height = lsb.low()

                V[0xf] = 0

                for (yline in 0..(height - 1)) {
                    val pixel = memory[I + 1]

                    for (xline in (0..7)) {
                        if ((pixel.toPositiveInt() and (0x80 shr xline)) != 0) {
                            val index = (x + xline + ((y + yline) * 64))
                            if (gfx[index] == 1) {
                                V[0xf] = 1
                            }

                            gfx[index] = gfx[index] xor 1
                        }
                    }
                }

                pc += 2
                println("DRW V${x.toHex()}, V${y.toHex()}, ${height.toHex()}")
            }
            else -> {

                println("! Unknown instruction")
                return stop()
            }
        }

        if (this.delayTimer > 0) delayTimer--
        if (this.soundTimer > 0) soundTimer--
    }
}