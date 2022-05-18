package ru.spbstu.edu.fogezz.moonsoon.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import ru.spbstu.edu.fogezz.moonsoon.databinding.FragmentChatBinding
import java.security.KeyPairGenerator
import javax.crypto.Cipher

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
        val adapter = ChatAdapter(requireContext())
        val viewModel: ChatViewModel by viewModels { ChatViewModelFactory(nickname, user) }

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

        binding.sendBtn.setOnClickListener{
            val text = binding.messageET.text.toString().trim()
            if(text.isNotBlank()) {
                viewModel.sendText(text)
//                KeyPairGenerator.getInstance("RSA").genKeyPair()
                binding.messageET.setText("")
            }
        }

        return binding.root
    }
}