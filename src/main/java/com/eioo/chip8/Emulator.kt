package com.eioo.chip8

import com.eioo.chip8.websocket.SocketServer

class Emulator(private val socketServer: SocketServer) {
    var memory: Memory = Memory(this)
    var cpu: CPU = CPU(this)

    init {
        reset()
    }

    fun broadcastToServer() {
        socketServer.broadcast(cpu.gfx.joinToString(""))
    }

    fun loadRom(path: String) = memory.loadRom(path) // Just an alias

    private fun reset() {
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