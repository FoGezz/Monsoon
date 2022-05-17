package ru.spbstu.edu.fogezz.moonsoon.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.spbstu.edu.fogezz.moonsoon.network.User

class ChatViewModelFactory(val nickname: String, val recipient: User) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(nickname, recipient) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}