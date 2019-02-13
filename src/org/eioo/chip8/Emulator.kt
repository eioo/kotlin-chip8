package org.eioo.chip8

class Emulator {
    var memory: Memory = Memory(this)
    var cpu: CPU = CPU(this)

    var running: Boolean = false
    var drawFlag: Boolean = false

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
        running = true
        println("Emulator started")

        val mainThread = Thread(Runnable {
            mainLoop()
        })

        mainThread.start()
    }

    fun stop() {
        running = false
        println("Emulator stopped")
    }

    fun mainLoop() {
        while (running) {
            cpu.performCycle()

            if (drawFlag) {
                println("Drawing gfx")
                drawFlag = false  // TODO: Implement
            }

            // TODO: Check keys here
        }
    }
}