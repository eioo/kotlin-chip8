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
        emu.stop()
        println("Socket disconnected")
    }

    override fun onMessage(conn: WebSocket?, message: String?) {
        val splitted = message!!.split(" ")
        val command = splitted[0]
        val params = splitted.subList(1, splitted.size)

        println("Socket ~> $message")

        when (command) {
            "loadrom" -> {
                val romName = params[0]
                val romPath = "roms/$romName"
                emu.stop()
                emu.reset()
                emu.broadcastVariables()
                emu.broadcastGraphics()
                emu.loadRom(romPath)
            }
            "changefreq" -> {
                emu.cpu.frequency = Integer.valueOf(splitted[1])
            }
            "start" -> {
                emu.start()
            }
            "stop" -> {
                emu.stop()
            }
            "reset" -> {
                emu.stop()
                emu.reset()

                if (emu.currentRom != "") {
                    emu.loadRom(emu.currentRom)
                }

                emu.broadcastVariables()
                emu.broadcastGraphics()
            }
            "keydown" -> {
                val index = Integer.parseInt(params[0])
                emu.cpu.key[index] = 1
            }
            "keyup" -> {
                val index = Integer.parseInt(params[0])
                emu.cpu.key[index] = 0
            }
            else -> println("Socket <~> Unknown command")
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