package ru.spbstu.edu.fogezz.moonsoon.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.spbstu.edu.fogezz.moonsoon.network.Network

class LoginViewModel : ViewModel() {
    private val _isLogged = MutableLiveData(false)
    val isLogged: LiveData<Boolean>
        get() = _isLogged


    fun enter(nickname: String) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) { Network.api.enter(nickname) }
            _isLogged.value = response.code() == 200
        }
    }

    fun loginNavigated() {
        _isLogged.value = false
    }
}