package ru.spbstu.edu.fogezz.moonsoon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.buttonLoginConnect.setOnClickListener{
            val isLogged = requestLogin()
            if(isLogged) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToListFragment(10))
            }
        }

        return binding.root
    }

    private fun requestLogin(): Boolean = runBlocking {
        val client = HttpClient(CIO)
        val response: HttpResponse = client.request("localhost/")
        // Configure request parameters exposed by HttpRequestBuilder

        return@runBlocking response.status.isSuccess()
    }
}