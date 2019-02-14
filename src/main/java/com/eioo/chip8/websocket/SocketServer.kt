package com.eioo.chip8.websocket

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import java.nio.ByteBuffer

class SocketServer(port: Int) : WebSocketServer(InetSocketAddress(port)) {
    public var running = false

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        println("Socket connected")
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        println("Socket disconnected")
    }

    override fun onMessage(conn: WebSocket?, message: String?) {
        println("$conn: $message")
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
        running = true
    }
}