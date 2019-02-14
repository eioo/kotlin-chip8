package com.eioo.chip8

open class Instructions(private var cpu: CPU, private var memory: Memory) {
    fun unknown() {
        cpu.running = false
        println("! Unknown instruction")
    }

    // region 0x0
    fun ret() {
        // 0x00EE - RET
        // Return from a subroutine.
        cpu.pc = cpu.stack[cpu.sp]
        cpu.sp--

        println("RET (sp: ${cpu.sp})")
    }
    // endregion

    // region 0x1
    fun setpc(addr: Int) {
        // 1nnn - JP addr
        // Jump to location nnn.
        cpu.pc = addr

        println("JP $addr")
    }

    // endregion

    // region 0x2
    fun call(addr: Int) {
        // 2nnn - CALL addr
        // Call subroutine at nnn.

        cpu.sp++
        cpu.stack[cpu.sp] = cpu.pc

        cpu.pc = addr

        println("CALL ${addr.toHex()} (sp: ${cpu.sp})")
    }
    // endregion

    // region 0x3
    // endregion

    // region 0x4
    // endregion

    // region 0x5
    // endregion

    // region 0x6
    fun set(reg: Int, value: Int) {
        // 6xkk - LD Vx, byte
        // Set Vx = kk.
        cpu.V[reg] = value
        cpu.pc += 2

        println("LD V${reg.toHex()}, ${value.toHex()}")
    }
    // endregion

    // region 0x7
    fun add(reg: Int, value: Int) {
        // 7xkk - ADD Vx, byte
        // Set Vx = Vx + kk
        cpu.V[reg] += value
        cpu.pc += 2

        println("ADD V$reg, $value")
    }
    // endregion

    // region 0x8
    // endregion

    // region 0x9
    // endregion

    // region 0xA
    fun seti(value: Int) {
        // Annn - LD I, addr
        // Set I = nnn.
        cpu.I = value
        cpu.pc += 2

        println("LD I, ${value.toHex()}")
    }
    // endregion

    // region 0xB
    fun jmp(addr: Int) {
        // Bnnn - JP V0, addr
        // Jump to location nnn + V0.
        cpu.pc = addr + cpu.V[0]

        println("JP V0, $addr")
    }
    // endregion

    // region 0xC
    // endregion

    // region 0xD
    fun draw(reg1: Int, reg2: Int, height: Int) {
        // Dxyn - DRW Vx, Vy, nibble
        // Display n-byte sprite starting at memory location I at (Vx, Vy), set VF = collision.
        cpu.V[0xf] = 0

        for (yline in 0..(height - 1)) {
            val pixel = memory.read(cpu.I + 1)

            for (xline in (0..7)) {
                if ((pixel.toPositiveInt() and (0x80 shr xline)) != 0) {
                    val index = (reg1 + xline + ((reg2 + yline) * 64))
                    if (cpu.gfx[index] == 1) {
                        cpu.V[0xf] = 1
                    }

                    cpu.gfx[index] = cpu.gfx[index] xor 1
                }
            }
        }

        cpu.drawFlag = true
        cpu.pc += 2
        println("DRW V${reg1.toHex()}, V${reg2.toHex()}, ${height.toHex()}")
    }
    // endregion

    // region 0xE
    // endregion

    // region 0xF
    // 0x29
    fun spritei(reg: Int) {
        // Fx29 - LD F, Vx
        // Set I = location of sprite for digit Vx.
        cpu.I = cpu.V[reg] * 5
        cpu.pc += 2

        println("LD F, V$reg")
    }

    // 0x33
    fun bcd(reg: Int) {
        // TODO: May be off
        // Fx33 - LD B, Vx
        // Store BCD representation of Vx in memory locations I, I+1, and I+2.
        val a = (cpu.V[reg].toDouble() / 100).toInt()
        val b = ((cpu.V[reg].toDouble() / 10.0).rem(10)).toInt()
        val c = (cpu.V[reg].rem(100)).rem(10)

        memory.write(cpu.I, a)
        memory.write(cpu.I + 1, b)
        memory.write(cpu.I + 1, c)

        cpu.pc += 2

        println("LD B, V$reg ($a, $b, $c)")
    }

    // 0x65
    fun read(reg: Int) {
        // Fx65 - LD Vx, [I]
        // Read registers V0 through Vx from memory starting at location I.
        for (i in 0 until reg) {
            cpu.V[i] = memory.read(cpu.I + i).toPositiveInt()
        }

        cpu.I += reg + 1
        cpu.pc += 2

        println("LD V$reg [I]")
    }
    //endregion
}