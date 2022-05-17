package ru.spbstu.edu.fogezz.moonsoon.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.spbstu.edu.fogezz.moonsoon.network.Network
import ru.spbstu.edu.fogezz.moonsoon.network.User

class ListViewModel : ViewModel() {
    val list = MutableLiveData<List<User>>()

    init {
        viewModelScope.launch {
            getList()
        }
    }

    suspend fun getList() {
        list.value =
            withContext(Dispatchers.IO) { Network.api.list() }.body()?.sortedBy { it.nickname }
    }
}