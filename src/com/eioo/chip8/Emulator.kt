package com.eioo.chip8

class Emulator {
    var memory: Memory = Memory(this)
    var cpu: CPU = CPU(this)

    init {
        reset()
    }

    // Just an alias
    fun loadRom(path: String) = memory.loadRom(path)

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