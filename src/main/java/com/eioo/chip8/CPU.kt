package com.eioo.chip8

import java.lang.System.currentTimeMillis

class CPU(private val emu: Emulator) {
    private var memory: Memory = emu.memory
    private var ins: Instructions = Instructions(this, memory)

    val gfx: IntArray = IntArray(64 * 32)   // Graphics, 1 bit for each pixelMemory
    val key: IntArray = IntArray(16)        // Pressed keys
    val V: IntArray = IntArray(16)          // Registers (V0, V1, ... VE) VE: Carry flag
    val stack: IntArray = IntArray(16)      // Stack
    var opcode: Int = 0                     // Current operation code
    var sp: Int = 0                         // Stack pointer
    var I: Int = 0                          // Index register
    var pc: Int = 0                         // Program counter (0x000 -> 0xFFF)
    var delayTimer: Int = 0                 // Counts to zero
    var soundTimer: Int = 0                 // Counts to zero

    private val frequency = 200             // CPU cycles per second (60 by default)
    var drawFlag: Boolean = false
    var running: Boolean = false

    fun reset() {
        pc = 0x200
        opcode = 0
        I = 0
        sp = 0
        delayTimer = 0
        soundTimer = 0

        for (i in 0 until V.size) V[i] = 0
        for (i in 0 until stack.size) stack[i] = 0
        for (i in 0 until gfx.size) gfx[i] = 0
        for (i in 0 until key.size) key[i] = 0
    }

    fun mainLoop() {
        var deltaTime: Long
        val sleepTime = 1000 / frequency

        while (running) {
            deltaTime = currentTimeMillis()
            performCycle()

            if (drawFlag) {
                emu.broadcastGraphics()
                drawFlag = false
            }

            // TODO: Check keys here

            // Throttle cycles
            deltaTime = currentTimeMillis() - deltaTime

            if (sleepTime - deltaTime > 0) {
                Thread.sleep(sleepTime - deltaTime)
            }
        }
    }

    private fun performCycle() {
        val msb = memory.read(pc)
        val lsb = memory.read(pc + 1)
        opcode = (msb.toInt() shl 8 or lsb.toPositiveInt()).and(0xFFFF)

        debug(pc.toHex().padEnd(10, ' ') + opcode.toHex().padStart(4, '0').padEnd(10, ' '))

        when (msb.high()) {
            0x0 -> {
                when (lsb.toPositiveInt()) {
                    0xEE -> ins.ret()
                    else -> ins.unknown()
                }
            }
            0x1 -> ins.setpc(opcode and 0xFFF)
            0x2 -> ins.call(opcode and 0xFFF)
            0x3 -> ins.skip(msb.low(), lsb.toPositiveInt())
            0x4 -> ins.skipne(msb.low(), lsb.toPositiveInt())
            0x5 -> ins.skipv(msb.low(), lsb.toPositiveInt())
            0x6 -> ins.set(msb.low(), lsb.toPositiveInt())
            0x7 -> ins.addv(msb.low(), lsb.toPositiveInt())
            0x8 -> {
                when (lsb.low()) {
                    0x0 -> ins.setv(msb.low(), lsb.high())
                    0x1 -> ins.or(msb.low(), lsb.high())
                    0x2 -> ins.and(msb.low(), lsb.high())
                    0x3 -> ins.xor(msb.low(), lsb.high())
                    0x4 -> ins.add(msb.low(), lsb.high())
                    0x5 -> ins.sub(msb.low(), lsb.high())
                    0x6 -> ins.shr(msb.low(), lsb.high())
                    0x7 -> ins.subn(msb.low(), lsb.high())
                    0xE -> ins.shl(msb.low(), lsb.high())
                    else -> ins.unknown()
                }
            }
            0xA -> ins.seti(opcode and 0xFFF)
            0xB -> ins.jmp(opcode and 0xFFF)
            0xC -> ins.rand(msb.low(), lsb.toPositiveInt())
            0xD -> ins.draw(msb.low(), lsb.high(), lsb.low())
            0xE -> {
                when (lsb.toPositiveInt()) {
                    0x9E -> ins.skipp(msb.low())
                    0xA1 -> ins.skipnp(msb.low())
                    else -> ins.unknown()
                }
            }
            0xF -> {
                when (lsb.toPositiveInt()) {
                    0x07 -> ins.getdelay(msb.low())
                    0x15 -> ins.setdelay(msb.low())
                    // 0x0A -> ins.waitkey(msb.low())
                    0x18 -> ins.setsound(msb.low())
                    0x1E -> ins.addi(msb.low())
                    0x29 -> ins.spritei(msb.low())
                    0x33 -> ins.bcd(msb.low())
                    0x55 -> ins.store(msb.low())
                    0x65 -> ins.read(msb.low())
                    else -> ins.unknown()
                }
            }
            else -> ins.unknown()
        }

        if (this.delayTimer > 0) delayTimer--
        if (this.soundTimer > 0) soundTimer--
    }
}