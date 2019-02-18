package com.eioo.chip8

import com.eioo.chip8.websocket.SocketServer
import io.github.cdimascio.dotenv.Dotenv

val dotenv = Dotenv.load()
var debugMode = false

fun main(args: Array<String>) {
    if ("--debug" in args) {
        debugMode = true
    }

    println(dotenv["PORT"])

    val emu = Emulator()
    val server = SocketServer(emu, Integer.parseInt(dotenv["PORT"]))
    emu.socketServer = server

    server.start()
}