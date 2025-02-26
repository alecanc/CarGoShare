package com.example.cargoshare.Ricerca

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cargoshare.databinding.FragmentCorsaAvviataBinding
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View.OnCreateContextMenuListener
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.setFragmentResult
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.Profilo.SchermataHome
import com.example.cargoshare.R
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CorsaAvviataFrag : AppCompatActivity() {
    private var isCoroutineRunning = false

    private var timerJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var targa:String?=null
    private var data:String?=null
    private var email:String?=null
    private var orario:String?=null
    private var durata:String?=null
    private var lastTimerValue: String ?=null
    private lateinit var binding: FragmentCorsaAvviataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCorsaAvviataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras: Bundle? = intent.extras
        if (extras != null) {

            targa = extras.getString("targa")
            data = extras.getString("data")
            orario = extras.getString("orario")
            email = extras.getString("email")

        }
            startTimerCoroutine()


            binding.Blocca.setOnClickListener {

                 durata = lastTimerValue

                timerJob?.cancel()
                binding.orarioText.text = durata

                var prezzo=0.0F

                val durataComponents = durata!!.split(":")
                val hours = durataComponents[0].toInt()
                val minutes = durataComponents[1].toInt()
                val seconds = durataComponents[2].toInt()

                val totalSeconds = hours * 3600 + minutes * 60 + seconds


                val query2 = "select tariffa from Macchina where targa= '$targa' ;"
                ClientNetwork.retrofit.ricerca(query2).enqueue(
                    object : Callback<JsonObject> {
                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                        ) {
                            if (response.isSuccessful) {
                                if ((response.body()?.get("queryset") as JsonArray).size() >= 1) {
                                    val queryset = response.body()?.get("queryset") as JsonArray
                                    var  tariffa = (queryset.get(0) as JsonObject).get("tariffa").asFloat
                                    prezzo = tariffa * totalSeconds
                                    Log.i("i","$prezzo + $tariffa + $totalSeconds")
                                }
                            }
                        }

                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                        }
                    })


                val query =
                    "update Prenotazione set prezzo=$prezzo, durata_corsa= '$durata' where data='$data' AND orario='$orario' AND\n" +
                            "id_macchina= '$targa' AND id_utente= '$email' ;" +
                            "update Macchina set prenotazione=0 where targa='$targa';\n"

                ClientNetwork.retrofit.modifica(query).enqueue(
                    object : Callback<JsonObject> {
                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                        ) {
                            if (response.isSuccessful) {


                                val intent= Intent(this@CorsaAvviataFrag, FatturaAvvenutaFragment::class.java)
                                intent.putExtra("email", "$email")
                                intent.putExtra("data", "$data")
                                intent.putExtra("orario", "$orario")
                                intent.putExtra("targa", "$targa")

                                intent.putExtra("durata_corsa", durata)
                                intent.putExtra("prezzo", prezzo)
                                startActivity(intent)





                                }

                        }

                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                        }


                    })

            }



    }










    private fun startTimerCoroutine() {
        timerJob = coroutineScope.launch {
            isCoroutineRunning = true
            var seconds = 0

            while (isActive) {
                delay(1000)
                updateTimerText(seconds)
                seconds++
            }
        }
    }
    private fun updateTimerText(seconds: Int) {
        val formattedTime = String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60)
        lastTimerValue = formattedTime
        binding.orarioText.text = formattedTime
        Log.i("da", "$formattedTime")
    }


}