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
                connect(nickname, recipient)
            }

            messages.value = (messages.value ?: emptyList()) + Message("Chat started", Message.From.SYSTEM)

            val defer = async {
                listenToEnemy(messages);
            }
            defer.invokeOnCompletion {
                messages.value = (messages.value ?: emptyList()) + Message("Chat ended", Message.From.SYSTEM)
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

    private suspend fun listenToEnemy(messages: MutableLiveData<List<Message>>) {
        while (!TcpProvider.readSock.isClosedForRead) {
            TcpProvider.readSock.awaitContent()
            val str = TcpProvider.readSock.readUTF8Line()
            if (str !== null) {
                messages.value = (messages.value ?: emptyList()) + Message(str, Message.From.ENEMY)
            }
        }
    }

    fun sendText(text: String) {
        viewModelScope.launch { withContext(Dispatchers.IO) { TcpProvider.msg(text) } }
        messages.value = (messages.value ?: emptyList()) + Message(text, Message.From.ME)
    }
}