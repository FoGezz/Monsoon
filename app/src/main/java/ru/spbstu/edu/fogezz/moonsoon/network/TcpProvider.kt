package ru.spbstu.edu.fogezz.moonsoon.network

import android.util.Log
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers

object TcpProvider {
    private lateinit var client: Socket
    val readSock = client.openReadChannel()
    private val writeSock = client.openWriteChannel(true)

    private suspend fun writeUtf8(str: String) {
        writeSock.writeStringUtf8("$str\n")
        writeSock.flush()
    }

    suspend fun introduce(nickname: String) {
        writeUtf8("iam: $nickname\n")
    }

    suspend fun msg(from: String, to: String, msg: String) {
        writeUtf8("msg: $from|$to|$msg")
    }

    suspend fun awaitEstablishing() {
        do {
            readSock.awaitContent()
            val str = readSock.readUTF8Line()
        } while (str != "agent ready")
    }

    suspend fun connectClient() {
        try {
            client = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp().connect("10.0.2.2", 9002)
        } catch (e: Exception) {
            Log.d(javaClass.simpleName, e.message!!)
            error("Unable to connect: ${e.message}")
        }
    }
}