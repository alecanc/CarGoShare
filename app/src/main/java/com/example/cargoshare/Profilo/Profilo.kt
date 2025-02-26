package com.example.cargoshare.Profilo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.cargoshare.Autenticazione.Email
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.databinding.ActivityProfiloBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Profilo : AppCompatActivity() {

    private lateinit var binding: ActivityProfiloBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfiloBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val query =
            "select nome, cognome, indirizzo, citta, cap,numero_telefonico, foto from  Utente where email ='${Email.getEmail()}';"
        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                            val url = ((response.body()
                                ?.get("queryset") as JsonArray).get(0) as JsonObject).get("foto").asString
                            val nome = ((response.body()
                                ?.get("queryset") as JsonArray).get(0) as JsonObject).get("nome").asString
                            val cognome = ((response.body()
                                ?.get("queryset") as JsonArray).get(0) as JsonObject).get("cognome").asString
                            val indirizzo = ((response.body()
                                ?.get("queryset") as JsonArray).get(0) as JsonObject).get("indirizzo").asString
                            val citta = ((response.body()
                                ?.get("queryset") as JsonArray).get(0) as JsonObject).get("citta").asString
                            val cap = ((response.body()
                                ?.get("queryset") as JsonArray).get(0) as JsonObject).get("cap").asInt
                            val numero_telefonico = ((response.body()
                                ?.get("queryset") as JsonArray).get(0) as JsonObject).get("numero_telefonico").asInt
                            binding.name.text = nome
                            Log.i("url", "$url")
                            binding.surname.text = cognome
                            binding.address.text = indirizzo
                            binding.city.text = citta
                            binding.cap.text = cap.toString()
                            binding.numerot.text = numero_telefonico.toString()
                            ClientNetwork.retrofit.getAvatar(url).enqueue(
                                object : Callback<ResponseBody> {
                                    override fun onResponse(call: Call<ResponseBody>,
                                        response: Response<ResponseBody>
                                    ) {
                                        if (response.isSuccessful) {
                                            var avatar: Bitmap? = null
                                            if (response.body() != null) {
                                                avatar = BitmapFactory.decodeStream(
                                                    response.body()?.byteStream()
                                                )

                                                binding.imageView4.setImageBitmap(avatar)

                                            }else{
                                                Log.i("non", "non funziona")
                                            }


                                        } else {
                                            Toast.makeText(
                                                this@Profilo,
                                                "Non funziona profilo",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                        TODO("Not yet implemented")
                                    }
                                })


                        }

                    }


                            binding.backArrow.setOnClickListener {
                                val intent = Intent(this@Profilo, SchermataHome::class.java)
                                startActivity(intent)
                                finish()
                            }


                        }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    TODO("Not yet implemented")
                }


            })
                }


            }
