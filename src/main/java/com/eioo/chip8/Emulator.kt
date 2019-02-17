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
        if (socketServer == null) {
            println("No socket server")
            return
        }

        socketServer!!.broadcast(cpu.gfx.joinToString(""))
    }

    fun broadcastVariables() {
        if (socketServer == null) {
            println("No socket server")
            return
        }

        val data = """
            {
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
    }

    fun start() {
        cpu.running = true

        val mainThread = Thread(Runnable {
            cpu.mainLoop()
        })

        mainThread.start()
        println("Emulator started")
    }

    fun stop() {
        cpu.running = false
        println("Emulator stopped")
    }
}