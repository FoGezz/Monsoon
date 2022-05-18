package ru.spbstu.edu.fogezz.moonsoon.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.spbstu.edu.fogezz.moonsoon.databinding.FragmentChatBinding

class ChatFragment : Fragment() {
    private lateinit var viewModel: ChatViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val args = ChatFragmentArgs.fromBundle(requireArguments())
        val user = args.user
        val nickname = args.nickname
        val binding = FragmentChatBinding.inflate(inflater)
        val adapter = ChatAdapter(requireContext())
        viewModel = ViewModelProvider(
            this,
            ChatViewModelFactory(nickname, user)
        ).get(ChatViewModel::class.java)

        binding.chatLV.adapter = adapter
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel._isConnecting.observe(viewLifecycleOwner) {
            it ?: return@observe
            if (!it) Toast.makeText(context, "Чат открыт", Toast.LENGTH_SHORT).show()
        }

        viewModel.messages.observe(viewLifecycleOwner) {
            adapter.clear()
            it?.let {
                adapter.addAll(it)
            }
        }

        binding.sendBtn.setOnClickListener {
            val text = binding.messageET.text.toString().trim()
            if (text.isNotBlank()) {
                viewModel.sendText(text)
                binding.messageET.setText("")
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        viewModel.closeConnetction()
        super.onDestroy()
    }
}