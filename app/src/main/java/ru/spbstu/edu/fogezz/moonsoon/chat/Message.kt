package ru.spbstu.edu.fogezz.moonsoon.chat

data class Message(val text: String, val from: From) {
    enum class From {
        ME,
        ENEMY
    }
}
