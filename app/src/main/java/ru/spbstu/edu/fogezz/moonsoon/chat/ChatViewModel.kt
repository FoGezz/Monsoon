package ru.spbstu.edu.fogezz.moonsoon.chat

import android.os.IBinder
import android.view.View
import androidx.lifecycle.*
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
            _isConnecting.value = false
            messages.value = listOf(
                Message("Hello there", Message.From.ME),
            )
            delay(2000)
            messages.value = (messages.value ?: emptyList()) + Message("General Kenobi", Message.From.ENEMY)
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

    fun sendText(text: String) {
        viewModelScope.launch { withContext(Dispatchers.IO) { TcpProvider.msg(text) } }
        messages.value = (messages.value ?: emptyList()) + Message(text, Message.From.ME)
    }
}