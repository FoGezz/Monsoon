package ru.spbstu.edu.fogezz.moonsoon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btn = findViewById<Button>(R.id.button_login_connect)

        btn.setOnClickListener {
            val isLogged = requestLogin()

        }
    }

    private fun requestLogin(): Boolean = runBlocking {
        val client = HttpClient(CIO)
        val response: io.ktor.client.statement.HttpResponse = client.request("localhost/")
        // Configure request parameters exposed by HttpRequestBuilder

        return@runBlocking response.status.isSuccess()
    }

}
