package ru.spbstu.edu.fogezz.moonsoon.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.spbstu.edu.fogezz.moonsoon.network.Network
import ru.spbstu.edu.fogezz.moonsoon.network.User

class ListViewModel(nickname: String) : ViewModel() {
    val list = MutableLiveData<List<User>>()
    private lateinit var deffered: Deferred<Unit>
    init {
        viewModelScope.launch {
            deffered = async {
                while (true)
                    getList(nickname)
            }
        }
    }

    private suspend fun getList(nickname: String) {
        val list = withContext(Dispatchers.IO) { Network.api.list() }.body() ?: emptyList()
        this.list.value = list.filterNot { it.nickname == nickname }.sortedBy { it.nickname }
    }

    fun stopUpdate(){
        viewModelScope.launch {
            deffered.cancelAndJoin()
        }
    }
}