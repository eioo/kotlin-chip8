package com.eioo.chip8

import java.net.URL
import java.util.ArrayList
import java.io.BufferedReader
import java.io.InputStreamReader


class ResourceUtils {
    fun getResource(path: String): URL? {
        return this::class.java.classLoader.getResource(path)
    }

    fun getResourceFiles(path: String): List<String> {
        val filenames = ArrayList<String>()

        this::class.java.classLoader.getResourceAsStream(path).use { `in` ->
            BufferedReader(InputStreamReader(`in`)).use { br ->
                var resource = br.readLine()

                while (resource != null) {
                    filenames.add(resource)
                    resource = br.readLine()
                }
            }
        }

        return filenames
    }
}