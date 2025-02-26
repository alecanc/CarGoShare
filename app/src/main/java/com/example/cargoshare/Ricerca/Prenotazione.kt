package com.example.cargoshare.Ricerca



import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cargoshare.databinding.ActivityPrenotazioneBinding


class Prenotazione : AppCompatActivity() {
    private lateinit var binding: ActivityPrenotazioneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrenotazioneBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val extras: Bundle? = intent.extras
        if (extras != null) {
            val   email = extras.getString("email")
            val  targa = extras.getString("targa")
            val   data = extras.getString("data")
            val   orario = extras.getString("orario")



            binding.sbloccaButton.setOnClickListener {

                val i = Intent(this@Prenotazione, Key::class.java)
                i.putExtra("email", email)
                i.putExtra("data", data)
                i.putExtra("orario", orario)
                i.putExtra("targa", targa)
                startActivity(i)
            }

        }

    }


}