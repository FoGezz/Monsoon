package ru.spbstu.edu.fogezz.moonsoon

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity() {

    private var editText: EditText? = null
    private var listView: ListView? = null
    private var stringMessage: String? = null
    private var cipher: Cipher? = null
    private var decipher: Cipher? = null
    private var secretKeySpec: SecretKeySpec? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


}