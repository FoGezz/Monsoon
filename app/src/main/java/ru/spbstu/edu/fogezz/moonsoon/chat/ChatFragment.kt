package ru.spbstu.edu.fogezz.moonsoon.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import ru.spbstu.edu.fogezz.moonsoon.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val args = ChatFragmentArgs.fromBundle(requireArguments())
        val user = args.user
        val nickname = args.nickname
        val binding = FragmentChatBinding.inflate(inflater)
        val viewModel: ChatViewModel by viewModels { ChatViewModelFactory(nickname) }

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel._isConnecting.observe(viewLifecycleOwner) {
            it ?: return@observe
            if (!it) Toast.makeText(context, "Мы внутри", Toast.LENGTH_SHORT).show()
        }

//        binding.tw.text = user.toString()

        return binding.root
    }
}