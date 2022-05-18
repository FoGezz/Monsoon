package ru.spbstu.edu.fogezz.moonsoon.network

import android.util.Base64
import android.util.Log
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.security.PublicKey

object TcpProvider {
    private const val HOSTNAME = "10.0.2.2"
    private const val PORT = 9002

    private var client: Socket = runBlocking {
        withContext(Dispatchers.IO) {
            aSocket(ActorSelectorManager(Dispatchers.IO)).tcp().connect(HOSTNAME, PORT)
        }
    }

    val readSock = client.openReadChannel()
    private val writeSock = client.openWriteChannel(true)
    val isOpenForReading: Boolean
        get() = !readSock.isClosedForRead

    private suspend fun writeUtf8(str: String) {
        writeSock.writeStringUtf8("$str\n")
        writeSock.flush()
    }

    suspend fun introduce(nickname: String) {
        writeUtf8("iam: $nickname\n")
    }

    suspend fun to(nickname: String) {
        writeUtf8("rcpt: $nickname\n")
    }

    suspend fun sendPKey(pkey: PublicKey) {
        val b64pkey = Base64.encodeToString(pkey.encoded,  Base64.NO_WRAP)
        Log.d("PKEY send", "\"$b64pkey\"")
        writeSock.writeStringUtf8("pkey: ${b64pkey}\n")
        writeSock.flush()
    }

    suspend fun msg(msg: String) {
        writeUtf8("msg: $msg")
    }

    suspend fun openConnection(from: String, rcpt: String) {
//        connectClient()
        introduce(from)
        to(rcpt)
        awaitEstablishing()
    }

    suspend fun waitString(): String? {
        readSock.awaitContent()
        return readSock.readUTF8Line()
    }

    suspend fun awaitEstablishing() {
        do {
            readSock.awaitContent()
            val str = readSock.readUTF8Line()
        } while (str != "agent ready")
    }

    suspend fun awaitKeyExchange(): String {
        while (true) {
            readSock.awaitContent()
            val str = readSock.readUTF8Line()
            if (str?.startsWith("pkey: ") == true) {
                return str.substring(6)
            }
        }
    }

    suspend fun connectClient() {
        try {
            client = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp().connect(HOSTNAME, PORT)
        } catch (e: Exception) {
            Log.d(javaClass.simpleName, e.message!!)
            error("Unable to connect: ${e.message}")
        }
    }

    fun closeConnection() {
        client.close()
    }
}