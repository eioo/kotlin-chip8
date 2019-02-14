package com.eioo.chip8

fun debug(message: Any?) {
    if (debugMode) {
        print(message)
    }
}

fun debugln(message: Any?) {
    if (debugMode) {
        println(message)
    }
}
