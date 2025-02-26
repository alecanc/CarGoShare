package com.example.cargoshare.Profilo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cargoshare.R
import com.example.cargoshare.databinding.ActivityAboutBinding
import com.example.cargoshare.databinding.ActivityFattureBinding

class About : AppCompatActivity() {


    private lateinit var binding : ActivityAboutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        val domandeRisposte = listOf(
            Domande(getString(R.string.domanda_1), getString(R.string.risposta_1)),
            Domande(getString(R.string.domanda_2), getString(R.string.risposta_2)),
            Domande(getString(R.string.domanda_3), getString(R.string.risposta_3)),
            Domande(getString(R.string.domanda_4), getString(R.string.risposta_4)),
            Domande(getString(R.string.domanda_5), getString(R.string.risposta_5)),
            Domande(getString(R.string.domanda_6), getString(R.string.risposta_6))
            )


        // Crea un'istanza dell'adattatore e imposta l'elenco di domande e risposte
        val adapter = CustomAdapter(domandeRisposte)
        binding.recyclerView.adapter = adapter


        binding.backArrow.setOnClickListener {
            val intent = Intent(this, SchermataHome::class.java)
            startActivity(intent)
            finish()
        }
    }

}

