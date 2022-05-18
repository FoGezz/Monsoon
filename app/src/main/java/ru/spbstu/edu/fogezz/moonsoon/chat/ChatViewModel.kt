package ru.spbstu.edu.fogezz.moonsoon.chat

import android.util.Base64
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.google.gson.JsonObject
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import ru.spbstu.edu.fogezz.moonsoon.network.TcpProvider
import ru.spbstu.edu.fogezz.moonsoon.network.User
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

class ChatViewModel(nickname: String, recipient: User) : ViewModel() {
    val _isConnecting = MutableLiveData<Boolean>()
    val loaderVisibility = _isConnecting.map { if (it) View.VISIBLE else View.GONE }
    val chatVisibility = _isConnecting.map { if (!it) View.VISIBLE else View.GONE }
    val isElementsEnabled = _isConnecting.map { !it }
    val messages = MutableLiveData<List<Message>>()

    val encryptor = Cipher.getInstance("RSA")
    val decryptor = Cipher.getInstance("RSA")

    lateinit var enemyKey: PublicKey
    lateinit var keyPair : KeyPair

    init {
        viewModelScope.launch {
            _isConnecting.value = true
            withContext(Dispatchers.IO) {
                connect(nickname, recipient)
//                TcpProvider.openConnection(nickname, recipient.nickname)
            }

            messages.add(Message("Chat started", Message.From.SYSTEM))
            val defer = async { listenToEnemy() }
            defer.invokeOnCompletion {
                messages.add(Message("Chat ended", Message.From.SYSTEM))
            }
            _isConnecting.value = false
        }
    }

    private suspend fun connect(nickname: String, recipient: User): Int {

        val generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        keyPair = generator.generateKeyPair();
        TcpProvider.introduce(nickname)
        TcpProvider.to(recipient.nickname)
        TcpProvider.awaitEstablishing()

        TcpProvider.sendPKey(keyPair.public)

        val base64EnemyKeyStr = TcpProvider.awaitKeyExchange()
        Log.d("PKEY rcv", "\"$base64EnemyKeyStr\"")
        val enemyKeyBytes = Base64.decode(base64EnemyKeyStr, Base64.NO_WRAP)
        val X509publicKey = X509EncodedKeySpec(enemyKeyBytes)

        val kf = KeyFactory.getInstance("RSA")
        enemyKey = kf.generatePublic(X509publicKey)

        encryptor.init(Cipher.ENCRYPT_MODE, enemyKey)
        decryptor.init(Cipher.DECRYPT_MODE, keyPair.private)

        return 0
    }

    private suspend fun listenToEnemy() {
        while (!TcpProvider.readSock.isClosedForRead) {
            val base64msg = TcpProvider.waitString()

            if (base64msg != null) {
                Log.d("base64log", base64msg)
                val encoded = Base64.decode(base64msg.trim(), Base64.NO_WRAP)
                val str = decryptor.doFinal(encoded).decodeToString()
                Log.d("strlog", str)
                messages.add(Message(str, Message.From.ENEMY))
            }
        }
    }

    fun sendText(text: String) {
        val msgBytes = text.toByteArray()
        val encryptedMsgBytes = encryptor.doFinal(msgBytes)
        val base64msg = Base64.encodeToString(encryptedMsgBytes, Base64.NO_WRAP).trim()
        Log.d("base64log", base64msg)
        viewModelScope.launch { withContext(Dispatchers.IO) { TcpProvider.msg(base64msg) } }
        messages.add(Message(text, Message.From.ME))
    }

    fun closeConnetction() {
        TcpProvider.closeConnection()
    }

}

private fun MutableLiveData<List<Message>>.add(message: Message) {
    value = (value ?: emptyList()) + message
}
