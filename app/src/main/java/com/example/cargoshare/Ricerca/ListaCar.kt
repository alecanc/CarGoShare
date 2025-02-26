package com.example.cargoshare.Ricerca

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.R
import com.example.cargoshare.databinding.ActivityListaCarBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaCar:AppCompatActivity() {

    private lateinit var binding: ActivityListaCarBinding
    private lateinit var adapter : AdapterCar
    val macchine= ArrayList<Car>()
    private var data:String?=null
    private var orario:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = AdapterCar(macchine)

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = adapter
        val extras: Bundle? = intent.extras
        if (extras != null) {
            data = extras.getString("data")
            orario = extras.getString("orario")


            loadCars()

        }


    }
    private fun loadCars() {
        val query =
            "(select targa, id_proprietario, num_posti, indirizzo, citta, colore, tipologia, marca, modello, cambio, tariffa, anno, latitudine, longitudine from Macchina  Where prenotazione = 0) UNION  (select targa, id_proprietario, num_posti, indirizzo, citta, colore, tipologia, marca, modello, cambio, tariffa, anno, latitudine, longitudine from Macchina JOIN Prenotazione Where prenotazione = 0 AND id_macchina=Targa AND Data='$data' AND Orario='$orario');"
        ClientNetwork.retrofit.ricerca(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        if ((response.body()?.get("queryset") as JsonArray).size() >= 1) {
                            val queryset = response.body()?.get("queryset") as JsonArray
                            Log.i("ciao", "${queryset.size()}")
                            for (i in 0 until queryset.size()) {
                                macchine.add(
                                    Car(

                                        (queryset.get(i) as JsonObject).get("cambio").asString,
                                        (queryset.get(i) as JsonObject).get("num_posti").asString,
                                        (queryset.get(i) as JsonObject).get("indirizzo").asString,
                                        (queryset.get(i) as JsonObject).get("tipologia").asString,
                                        (queryset.get(i) as JsonObject).get("tariffa").asString,
                                        (queryset.get(i) as JsonObject).get("marca").asString,
                                        (queryset.get(i) as JsonObject).get("modello").asString,
                                        (queryset.get(i) as JsonObject).get("anno").asString,
                                        (queryset.get(i) as JsonObject).get("latitudine").asDouble,
                                        (queryset.get(i) as JsonObject).get("longitudine").asDouble
                                    )

                                )


                                binding.recyclerview.adapter = adapter
                                val layoutManager = LinearLayoutManager(this@ListaCar)
                                binding.recyclerview.layoutManager = layoutManager
                                adapter.setOnItemClickListener(object: AdapterCar.onItemClickListener{
                                    override fun onItemClick(position: Int){
                                          val mappa= Intent()
                                        mappa.putExtra("latitudine", macchine[position].latitudine.toDouble())
                                        mappa.putExtra("longitudine", macchine[position].longitudine.toDouble())
                                        Log.i("fsdf", "${macchine[position].longitudine}")
                                       setResult(Activity.RESULT_OK, mappa)
                                       finish()


                                    } })
                            }

                        } else {
                            Toast.makeText(
                                this@ListaCar,
                                "errore",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    } else {
                        Toast.makeText(
                           this@ListaCar,
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





