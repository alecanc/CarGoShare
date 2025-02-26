package com.example.cargoshare.Profilo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.Prenotazioni.Adapter
import com.example.cargoshare.Prenotazioni.Prenotazione
import com.example.cargoshare.R
import com.example.cargoshare.databinding.ActivityFattureBinding
import com.example.cargoshare.databinding.FragmentPrenotazioniBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Fatture : AppCompatActivity() {
    private lateinit var binding : ActivityFattureBinding
    private lateinit var adapter : AdapterFatture
    val data= ArrayList<Fattura>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
binding=ActivityFattureBinding.inflate(layoutInflater)
        setContentView(binding.root)
                adapter = AdapterFatture(data)

                val layoutManager = LinearLayoutManager(this)
                binding.recyclerview.layoutManager = layoutManager
                binding.recyclerview.adapter = adapter

                caricaFatture()
        binding.backArrow.setOnClickListener {
            val int= Intent(this@Fatture,SchermataHome::class.java)
            startActivity(int)
        }
            }




            private fun caricaFatture() {


                val query =
                    " select prezzo, data from Prenotazione where id_utente='cancemialessia@icloud.com';"
                ClientNetwork.retrofit.ricerca(query).enqueue(
                    object : Callback<JsonObject> {
                        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                            if (response.isSuccessful) {
                                if ((response.body()?.get("queryset") as JsonArray).size() >= 1) {
                                    val queryset = response.body()?.get("queryset") as JsonArray

                                    for (i in 0 until queryset.size()) {
                                        data.add(
                                            Fattura(
                                                (queryset.get(i) as JsonObject).get("data").asString,
                                                (queryset.get(i) as JsonObject).get("prezzo").asString,
                                            )
                                        )


                                        binding.recyclerview.adapter = adapter
                                        val layoutManager = LinearLayoutManager(this@Fatture)
                                        binding.recyclerview.layoutManager = layoutManager

                                    }

                                } else {
                                    Toast.makeText(
                                        this@Fatture,
                                        "errore",
                                        Toast.LENGTH_LONG
                                    ).show()

                                }
                            } else {
                                Toast.makeText(
                                    this@Fatture,
                                    "Errore nella risposta:",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {


                        }
                    }
                )



            }
}