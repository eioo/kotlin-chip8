package com.eioo.chip8.websocket

import com.eioo.chip8.Emulator
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import java.nio.ByteBuffer

class SocketServer(private val emu: Emulator, port: Int) : WebSocketServer(InetSocketAddress(port)) {
    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        println("Socket connected")
        emu.broadcastRoms()
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        println("Socket disconnected")
    }

    override fun onMessage(conn: WebSocket?, message: String?) {
        val splitted = message!!.split(" ")
        val command = splitted[0]

        when (command) {
            "loadrom" -> {
                val romName = splitted[1]
                val romPath = "roms/$romName"
                emu.stop()
                emu.reset()
                emu.broadcastVariables()
                emu.broadcastGraphics()
                emu.loadRom(romPath)
            }
            "start" -> {
                println("Socket ~> Start")
                emu.start()
            }
            "stop" -> {
                println("Socket ~> Stop")
                emu.stop()
            }
            "step" -> {
                // TODO: Implement
                println("Socket ~> Step")
            }
            "reset" -> {
                println("Socket ~> Reset")
                emu.stop()
                emu.reset()

                if (emu.currentRom != "") {
                    emu.loadRom(emu.currentRom)
                }

                emu.broadcastVariables()
                emu.broadcastGraphics()
            }
            else -> println("$conn: $message")
        }
    }

    override fun onMessage(conn: WebSocket?, message: ByteBuffer) {
        println("$conn: $message")
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {
        ex?.printStackTrace()

        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    override fun onStart() {
        System.out.println("Socket server started")
        connectionLostTimeout = 0
        connectionLostTimeout = 100
    }
}