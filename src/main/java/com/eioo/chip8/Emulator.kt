package com.eioo.chip8

import com.eioo.chip8.websocket.SocketServer

class Emulator(private val socketServer: SocketServer) {
    var memory: Memory = Memory(this)
    var cpu: CPU = CPU(this)

    init {
        reset()
    }

    fun broadcastGraphics() {
        socketServer.broadcast(cpu.gfx.joinToString(""))
    }

    fun broadcastVariables() {
        // TODO: Add better serialization
        var data = ""

        data += "${cpu.pc};"
        data += "${cpu.I};"
        data += "${cpu.sp};"
        data += "${cpu.delayTimer};"
        data += "${cpu.soundTimer};"
        data += "${cpu.stack.joinToString(",")};"
        data += "${cpu.V.joinToString(",")};"

        socketServer.broadcast(data)
    }

    fun loadRom(path: String) = memory.loadRom(path) // Just an alias

    private fun reset() {
        memory.reset()
        cpu.reset()
    }

    fun start() {
        cpu.running = true

        val mainThread = Thread(Runnable {
            if (debugMode) {
                cpu.debugLoop()
            } else {
                cpu.mainLoop()
            }
        })

        mainThread.start()
        println("Emulator started")
    }

    fun stop() {
        cpu.running = false
        println("Emulator stopped")
    }
}