package com.eioo.chip8

open class Instructions(private var cpu: CPU, private var memory: Memory) {
    fun unknown() {
        cpu.running = false
        debugln("! Unknown instruction")
    }

    // region 0x0
    fun sys() {
        // 0nnn - SYS addr
        // Jump to a machine code routine at nnn.

        // Do nothing here
        cpu.pc += 2

        debugln("SYS")
    }

    fun cls() {
        // 0x00E0 - CLS
        // Clear the display.
        cpu.clearGraphics()
        cpu.pc += 2

        debugln("CLS")
    }

    fun ret() {
        // 0x00EE - RET
        // Return from a subroutine.
        cpu.sp--
        cpu.pc = cpu.stack[cpu.sp]
        cpu.pc += 2

        debugln("RET (sp: ${cpu.sp})")
    }
    // endregion

    // region 0x1
    fun setpc(addr: Int) {
        // 1nnn - JP addr
        // Jump to location nnn.
        cpu.pc = addr

        debugln("JP ${addr.toHex()}")
    }

    // endregion

    // region 0x2
    fun call(addr: Int) {
        // 2nnn - CALL addr
        // Call subroutine at nnn.

        cpu.stack[cpu.sp] = cpu.pc
        cpu.sp++
        cpu.pc = addr

        debugln("CALL ${addr.toHex()} (sp: ${cpu.sp})")
    }
    // endregion

    // region 0x3
    fun skip(reg: Int, value: Int) {
        // 3xkk - SE Vx, byte
        // Skip next instruction if Vx = kk.
        cpu.pc += if (cpu.V[reg] == value) 4 else 2

        debugln("SE V${reg.toHex()}, ${value.toHex()}")
    }
    // endregion

    // region 0x4
    fun skipne(reg: Int, value: Int) {
        // 4xkk - SNE Vx, byte
        // Skip next instruction if Vx != kk.
        cpu.pc += if (cpu.V[reg] != value) 4 else 2

        debugln("SNE V${reg.toHex()}, ${value.toHex()}")
    }
    // endregion

    // region 0x5
    fun skipv(reg1: Int, reg2: Int) {
        // 5xy0 - SE Vx, Vy
        // Skip next instruction if Vx = Vy.
        debugln(reg1)
        debugln(reg2)
        cpu.pc += if (cpu.V[reg1] == cpu.V[reg2]) 4 else 2

        debugln("SE V${reg1.toHex()}, V${reg2.toHex()}")
    }
    // endregion

    // region 0x6
    fun set(reg: Int, value: Int) {
        // 6xkk - LD Vx, byte
        // Set Vx = kk.
        cpu.V[reg] = value
        cpu.pc += 2

        debugln("LD V${reg.toHex()}, ${value.toHex()}")
    }
    // endregion

    // region 0x7
    fun addv(reg: Int, value: Int) {
        // 7xkk - ADD Vx, byte
        // Set Vx = Vx + kk
        cpu.V[reg] += value
        cpu.pc += 2

        debugln("ADD V${reg.toHex()}, ${value.toHex()}")
    }
    // endregion

    // region 0x8
    fun setv(reg1: Int, reg2: Int) {
        // 8xy0 - LD Vx, Vy
        // Set Vx = Vy.
        cpu.V[reg1] = cpu.V[reg2]
        cpu.pc += 2

        debugln("Set V${reg1.toHex()} = V${reg2.toHex()}")
    }

    fun or(reg1: Int, reg2: Int) {
        // 8xy1 - XOR Vx, Vy
        // Set Vx = Vx XOR Vy.
        cpu.V[reg1] = cpu.V[reg1] and cpu.V[reg2]
        cpu.pc += 2

        debugln("Set V${reg1.toHex()} = V${reg1.toHex()} OR V${reg2.toHex()}")
    }

    fun and(reg1: Int, reg2: Int) {
        // 8xy2 - AND Vx, Vy
        // Set Vx = Vx AND Vy.
        cpu.V[reg1] = cpu.V[reg1] and cpu.V[reg2]
        cpu.pc += 2

        debugln("Set V${reg1.toHex()} = V${reg1.toHex()} AND V${reg2.toHex()}")
    }

    fun xor(reg1: Int, reg2: Int) {
        // 8xy3 - XOR Vx, Vy
        // Set Vx = Vx XOR Vy.
        cpu.V[reg1] = cpu.V[reg1] and cpu.V[reg2]
        cpu.pc += 2

        debugln("Set V${reg1.toHex()} = V${reg1.toHex()} XOR V${reg2.toHex()}")
    }

    fun add(reg1: Int, reg2: Int) {
        // 8xy4 - ADD Vx, Vy
        // Set Vx = Vx + Vy, set VF = carry.
        cpu.V[0xF] = if (cpu.V[reg2] > (0xFF - cpu.V[reg1])) 1 else 0
        cpu.V[reg1] += cpu.V[reg2] and 0xFF // TODO: Not sure if correct
        cpu.pc += 2

        debugln("ADD V${reg1.toHex()}, V${reg2.toHex()} (VF: ${cpu.V[0xF].toHex()})")
    }

    fun sub(reg1: Int, reg2: Int) {
        // 8xy5 - SUB Vx, Vy
        // Set Vx = Vx - Vy, set VF = NOT borrow.
        cpu.V[0xF] = if (cpu.V[reg1] > cpu.V[reg2]) 1 else 0
        cpu.V[reg1] = (cpu.V[reg1] - cpu.V[reg2]) and 0xFF // TODO: Not sure if correct
        cpu.pc += 2

        debugln("SUB V${reg1.toHex()}, V${reg2.toHex()} (VF: ${cpu.V[0xF].toHex()})")
    }

    fun shr(reg1: Int, reg2: Int) {
        // 8xy6 - SHR Vx {, Vy}
        // Set Vx = Vx SHR 1.
        cpu.V[0xF] = cpu.V[reg1] and 0x1
        cpu.V[reg1] = cpu.V[reg1] shr 1
        cpu.pc += 2

        debugln("SHR V${reg1.toHex()} {, V${reg2.toHex()}}")
    }

    fun subn(reg1: Int, reg2: Int) {
        // 8xy7 - SUBN Vx, Vy
        // Set Vx = Vy - Vx, set VF = NOT borrow.
        cpu.V[0xF] = if (cpu.V[reg1] < cpu.V[reg2]) 1 else 0
        cpu.V[reg1] = (cpu.V[reg2] - cpu.V[reg1]) and 0xFF // TODO: Not sure if correct
        cpu.pc += 2

        debugln("SUBN V${reg1.toHex()}, V${reg2.toHex()} (VF: ${cpu.V[0xF].toHex()})")
    }


    fun shl(reg1: Int, reg2: Int) {
        // 8xyE - SHL Vx {, Vy}
        // Set Vx = Vx SHL 1.
        cpu.V[0xF] = cpu.V[reg1] shr 7
        cpu.V[reg1] = cpu.V[reg1] shr 1
        cpu.pc += 2

        debugln("SHL V${reg1.toHex()} {, V${reg2.toHex()}}")
    }

    // endregion

    // region 0x9
    // endregion

    // region 0xA
    fun seti(value: Int) {
        // Annn - LD I, addr
        // Set I = nnn.
        cpu.I = value
        cpu.pc += 2

        debugln("LD I, ${value.toHex()}")
    }
    // endregion

    // region 0xB
    fun jmp(addr: Int) {
        // Bnnn - JP V0, addr
        // Jump to location nnn + V0.
        cpu.pc = addr + cpu.V[0]

        debugln("JP V0, ${addr.toHex()}")
    }
    // endregion

    // region 0xC
    fun rand(reg: Int, value: Int) {
        // Cxkk - RND Vx, byte
        // Set Vx = random byte AND kk.
        cpu.V[reg] = (0..255).random() and value
        cpu.pc += 2
        debugln("RND V${reg.toHex()}, ${value.toHex()}")
    }
    // endregion

    // region 0xD
    fun draw(reg1: Int, reg2: Int, height: Int) {
        // Dxyn - DRW Vx, Vy, nibble
        // Display n-byte sprite starting at memory location I at (Vx, Vy), set VF = collision.

        val x = cpu.V[reg1].and(0xff)
        val y = cpu.V[reg2].and(0xff)
        cpu.V[0xF] = 0

        for (yy in 0 until height) {
            val pixel = memory.read(cpu.I + yy).toPositiveInt()

            for (xx in 0 until 8) {
                if ((pixel and (0x80 shr xx)) != 0) {
                    val index = (x + xx + ((y + yy) * 64))

                    if (cpu.gfx[index] == 1) {
                        cpu.V[0xF] = 1
                    }

                    cpu.gfx[index] = cpu.gfx[index] xor 1
                }
            }
        }

        cpu.drawFlag = true
        cpu.pc += 2

        debugln("DRW V${reg1.toHex()}, V${reg2.toHex()}, $height (x: $x, y: $y)")
    }
    // endregion

    // region 0xE
    fun skipp(reg: Int) {
        // Ex9E - SKP Vx
        // Skip next instruction if key with the value of Vx pressed.
        cpu.pc += if (cpu.key[cpu.V[reg]] == 1) 4 else 2

        debugln("SKP V${reg.toHex()}")
    }

    fun skipnp(reg: Int) {
        // ExA1 - SKNP Vx
        // Skip next instruction if key with the value of Vx is not pressed.
        cpu.pc += if (cpu.key[cpu.V[reg]] == 0) 4 else 2

        debugln("SKNP V${reg.toHex()}")
    }
    // endregion

    // region 0xF
    // 0x07
    fun getdelay(reg: Int) {
        // Fx07 - LD Vx, DT
        // Set Vx = delay timer value.
        cpu.V[reg] = cpu.delayTimer
        cpu.pc += 2

        debugln("LD V${reg.toHex()}, DT")
    }

    // 0x15
    fun setdelay(reg: Int) {
        // Fx15 - LD DT, Vx
        // Set delay timer = Vx.
        cpu.delayTimer = cpu.V[reg]
        cpu.pc += 2

        debugln("LD DT, V${reg.toHex()}")
    }

    // 0x18
    fun setsound(reg: Int) {
        // Fx18 - LD ST, Vx
        // Set sound timer = Vx.
        cpu.soundTimer = cpu.V[reg]
        cpu.pc += 2

        debugln("LD DT, V${reg.toHex()}")
    }

    // 0x1E
    fun addi(reg: Int) {
        // Fx1E - ADD I, Vx
        // Set I = I + Vx.
        cpu.I += cpu.V[reg]
        cpu.pc += 2

        debugln("ADD I, V${reg.toHex()}")
    }

    // 0x29
    fun spritei(reg: Int) {
        // Fx29 - LD F, Vx
        // Set I = location of sprite for digit Vx.
        cpu.I = cpu.V[reg] * 5
        cpu.pc += 2

        debugln("LD F, V${reg.toHex()}")
    }

    // 0x33
    fun bcd(reg: Int) {
        // TODO: May be off
        // Fx33 - LD B, Vx
        // Store BCD representation of Vx in memory locations I, I+1, and I+2.
        val a = (cpu.V[reg].toDouble() / 100.0).rem(10).toInt()
        val b = (cpu.V[reg].toDouble() / 10.0).rem(10).toInt()
        val c = (cpu.V[reg].toDouble() / 1.0).rem(10).toInt()

        memory.write(cpu.I + 0, a)
        memory.write(cpu.I + 1, b)
        memory.write(cpu.I + 2, c)

        cpu.pc += 2

        debugln("LD B, V${reg.toHex()} (${a.toHex()}, ${b.toHex()}, ${c.toHex()})")
    }

    // 0x55
    fun store(reg: Int) {
        // TODO: May be wrong
        // Fx55 - LD [I], Vx
        // Store registers V0 through Vx in memory starting at location I.
        for (i in 0 until reg) {
            memory.write(cpu.I + i, cpu.V[i])
        }

        cpu.I += reg + 1
        cpu.pc += 2

        debugln("LD [I], V${reg.toHex()}")
    }

    // 0x65
    fun read(reg: Int) {
        // TODO: May be wrong
        // Fx65 - LD Vx, [I]
        // Read registers V0 through Vx from memory starting at location I.
        for (i in 0 until reg) {
            cpu.V[i] = memory.read(cpu.I + i).toPositiveInt()
        }

        cpu.I += reg + 1
        cpu.pc += 2

        debugln("LD V${reg.toHex()} [I]")
    }
    //endregion
}