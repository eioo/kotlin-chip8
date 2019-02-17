package com.eioo.chip8

import com.eioo.chip8.websocket.SocketServer

class Emulator {
    var memory: Memory = Memory(this)
    var cpu: CPU = CPU(this)
    var socketServer: SocketServer? = null

    var currentRom = ""

    init {
        reset()
    }

    fun broadcastGraphics() {
        socketServer!!.broadcast(cpu.gfx.joinToString(""))
    }

    fun broadcastVariables() {
        val emuState = cpu.running.toString()
        val data = """
            {
                "emustate": $emuState,
                "pc": ${cpu.pc},
                "i": ${cpu.I},
                "sp": ${cpu.sp},
                "dt": ${cpu.delayTimer},
                "st": ${cpu.soundTimer},
                "stack": [${cpu.stack.joinToString(",")}],
                "v": [${cpu.V.joinToString(",")}]
            }
        """.trimIndent()

        socketServer!!.broadcast(data)
    }

    fun broadcastRoms() {
        val roms = ResourceUtils().getResourceFiles("roms")
        val romString = "[\"${roms.joinToString("\",\"")}\"]"

        socketServer!!.broadcast("{ \"roms\": $romString }")
    }

    fun loadRom(path: String) {
        currentRom = path
        memory.loadRom(path)
    }

    fun reset() {
        memory.reset()
        cpu.reset()
        println("Emulator reset")
    }

    fun start() {
        cpu.running = true

        Thread(Runnable {
            cpu.mainLoop()
        }).start()

        println("Emulator started")
    }

    fun stop() {
        cpu.running = false
        broadcastVariables()
        println("Emulator stopped")
    }
}