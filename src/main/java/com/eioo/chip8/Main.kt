package com.eioo.chip8

import com.eioo.chip8.websocket.SocketServer

var debugMode = false

fun main(args: Array<String>) {
    if ("--debug" in args) {
        debugMode = true
    }

    val server = SocketServer(8080)
    server.start()

    while (!server.running) {
        Thread.sleep(10)
    }

    val emu = Emulator(server)
    emu.loadRom("roms/pong.rom")
    emu.start()
}