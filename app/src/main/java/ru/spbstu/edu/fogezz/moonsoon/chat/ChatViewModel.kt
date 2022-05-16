package ru.spbstu.edu.fogezz.moonsoon.chat

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.spbstu.edu.fogezz.moonsoon.network.TcpProvider

class ChatViewModel(nickname: String) : ViewModel() {
    val _isConnecting = MutableLiveData<Boolean>()
    val loaderVisibility = _isConnecting.map { if (it) View.VISIBLE else View.GONE }

    init {
        viewModelScope.launch {
            _isConnecting.value = true
            withContext(Dispatchers.IO) {
                connect(nickname)
            }
            _isConnecting.value = false
        }
    }

    private suspend fun connect(nickname: String): Int {
//        delay(3 * 1000L)
//        Thread.sleep(3*1000L)
        TcpProvider.connectClient()
        TcpProvider.introduce(nickname)
        TcpProvider.awaitEstablishing()
        return 0
    }
}