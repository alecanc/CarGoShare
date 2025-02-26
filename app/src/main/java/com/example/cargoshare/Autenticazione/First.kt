package com.example.cargoshare.Autenticazione

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cargoshare.R
import com.example.cargoshare.databinding.ActivityFirstBinding

class First : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login1Button.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        binding.registrationButton.setOnClickListener {
            val intent = Intent(this, Registrazione::class.java)
            startActivity(intent)
        }

    }
}