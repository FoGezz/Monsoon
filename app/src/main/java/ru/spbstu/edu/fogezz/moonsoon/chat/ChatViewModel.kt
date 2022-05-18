package ru.spbstu.edu.fogezz.moonsoon.chat

import android.os.IBinder
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import ru.spbstu.edu.fogezz.moonsoon.network.TcpProvider
import ru.spbstu.edu.fogezz.moonsoon.network.User

class ChatViewModel(nickname: String, recipient: User) : ViewModel() {
    val _isConnecting = MutableLiveData<Boolean>()
    val loaderVisibility = _isConnecting.map { if (it) View.VISIBLE else View.GONE }
    val chatVisibility = _isConnecting.map { if (!it) View.VISIBLE else View.GONE }
    val isElementsEnabled = _isConnecting.map { !it }
    val messages = MutableLiveData<List<Message>>()

    init {
        viewModelScope.launch {
            _isConnecting.value = true
            withContext(Dispatchers.IO) {
//                connect(nickname, recipient)
                TcpProvider.openConnection(nickname, recipient.nickname)
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
//        delay(3 * 1000L)
//        Thread.sleep(3*1000L)
//        TcpProvider.connectClient()
        TcpProvider.introduce(nickname)
        TcpProvider.to(recipient.nickname)
        TcpProvider.awaitEstablishing()
        return 0
    }

    private suspend fun listenToEnemy() {
        while (!TcpProvider.readSock.isClosedForRead) {
            val str = TcpProvider.waitString()
            if (str !== null) {
                messages.add(Message(str, Message.From.ENEMY))
            }
        }
    }

    fun sendText(text: String) {
        viewModelScope.launch { withContext(Dispatchers.IO) { TcpProvider.msg(text) } }
        messages.add(Message(text, Message.From.ME))
    }

    fun closeConnetction() {
        TcpProvider.closeConnection()
    }

}

private fun MutableLiveData<List<Message>>.add(message: Message) {
    value = (value ?: emptyList()) + message
}
