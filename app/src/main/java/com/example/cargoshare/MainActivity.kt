package com.example.cargoshare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cargoshare.Autenticazione.First
import com.example.cargoshare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iniziaButton.setOnClickListener {
            val intent = Intent(this, First ::class.java)
            startActivity(intent)
        }

    }


}
