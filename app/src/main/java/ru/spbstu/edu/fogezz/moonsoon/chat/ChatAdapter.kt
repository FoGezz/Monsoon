package ru.spbstu.edu.fogezz.moonsoon.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import ru.spbstu.edu.fogezz.moonsoon.databinding.EnemyChatMessageBinding
import ru.spbstu.edu.fogezz.moonsoon.databinding.MyChatMessageBinding
import ru.spbstu.edu.fogezz.moonsoon.databinding.SystemChatMessageBinding

class ChatAdapter(context: Context) : ArrayAdapter<Message>(context, 0) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val message = getItem(position)!!

        return when (message.from) {
            Message.From.ME -> MyChatMessageBinding.inflate(LayoutInflater.from(context))
                .run {
                    messageTV.text = message.text
                    root
                }
            Message.From.ENEMY -> EnemyChatMessageBinding.inflate(LayoutInflater.from(context))
                .run {
                    enemyMessageTV.text = message.text
                    root
                }
            Message.From.SYSTEM -> SystemChatMessageBinding.inflate(LayoutInflater.from(context))
                .run {
                    systemMessageTV.text = message.text
                    root
                }
        }
    }
}