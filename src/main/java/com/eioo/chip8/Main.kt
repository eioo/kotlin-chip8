package com.eioo.chip8

import com.eioo.chip8.websocket.SocketServer

var debugMode = false

fun main(args: Array<String>) {
    if ("--debug" in args) {
        debugMode = true
    }

    val emu = Emulator()
    val server = SocketServer(emu, 8080)
    emu.socketServer = server

    server.start()
}