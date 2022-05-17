package ru.spbstu.edu.fogezz.moonsoon.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import ru.spbstu.edu.fogezz.moonsoon.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentLoginBinding.inflate(inflater)
        val viewModel: LoginViewModel by viewModels()

        binding.buttonLoginConnect.setOnClickListener {
            val nickname = binding.edittextLoginUserId.text.toString().trim()
            if (nickname.isNotBlank())
                viewModel.enter(nickname)
        }
        viewModel.isLogged.observe(viewLifecycleOwner) {
            it ?: return@observe

            if (it) {
                val nickname = binding.edittextLoginUserId.text.toString().trim()
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToListFragment(nickname)
                )
//                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToChatFragment(
//                    User("coolest")
//                ))
                viewModel.loginNavigated()
            }
        }

        return binding.root
    }

    private fun requestLogin(): Boolean = runBlocking {
        val client = HttpClient(CIO)
        val response: HttpResponse = client.request("http://10.0.2.2:8000/enter?nickname=FoGezz")
        // Configure request parameters exposed by HttpRequestBuilder

        return@runBlocking response.status.isSuccess()
    }
}