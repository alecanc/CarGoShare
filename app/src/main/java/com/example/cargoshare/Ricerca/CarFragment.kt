package com.example.cargoshare.Ricerca



import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ScrollView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.example.cargoshare.Autenticazione.Email
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.databinding.FragmentCarBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*

class CarFragment : Fragment() {

    private lateinit var binding: FragmentCarBinding
    private lateinit var data: String
    private lateinit var orario: String
    private var avatar:Bitmap? = null
    private var avatar2 :Bitmap? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCarBinding.inflate(inflater)


        parentFragmentManager.setFragmentResultListener("carInfo", this) { requestKey, bundle ->
            val targa = bundle.getString("targa")
            val posti = bundle.getInt("num_posti")
            val indirizzo = bundle.getString("indirizzo")
            val citta = bundle.getString("citta")
            val colore = bundle.getString("colore")
            val tipologia = bundle.getString("tipologia")
            val marca = bundle.getString("marca")
            val modello = bundle.getString("modello")
            val cambio = bundle.getString("cambio")
            val foto = bundle.getString("foto")
            val foto2 = bundle.getString("foto_p")
            val tariffa = bundle.getFloat("tariffa")
            val anno = bundle.getString("anno")
            val email_p = bundle.getString("email_p")
            val nome_p = bundle.getString("nome_p")
            val cognome_p = bundle.getString("cognome_p")
            val numero_tel = bundle.getInt("numero_tel")
            val stelle = bundle.getInt("stelle").toFloat()
            val descrizione = bundle.getString("descrizione")
            data = bundle.getString("data").toString()
            orario = bundle.getString("orario").toString()
            Log.i("foto", "$foto2")


            if (foto != null) {
                ClientNetwork.retrofit.getAvatar(foto).enqueue(
                    object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {

                                if (response.body() != null) {
                                    avatar = BitmapFactory.decodeStream(
                                        response.body()?.byteStream()
                                    )
binding.fotomacchina.setImageBitmap(avatar)

                                } else {
                                    Log.i("non", "non funziona")
                                }


                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
            }
            if (foto2 != null) {
            ClientNetwork.retrofit.getAvatar(foto2).enqueue(
                object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>,
                                            response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {

                            if (response.body() != null) {
                                avatar2 = BitmapFactory.decodeStream(
                                    response.body()?.byteStream()

                                )

                                binding.prof.setImageBitmap(avatar2)
                            }else{
                                Log.i("non", "non funziona")
                            }


                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
        }
            binding.targa.text = targa

            binding.numPosti.text = posti.toString()
            binding.indirizzo.text = indirizzo
            binding.citta.text = citta
            binding.colore.text = colore

            binding.tipologia.text = tipologia
            binding.marca.text = marca
            binding.modello.text = modello.toString()
            binding.cambio.text = cambio
            binding.tariffa.text = (tariffa.toString())
            binding.anno.text = anno
            binding.modello.text = modello.toString()
            binding.email.text = email_p
            binding.nomeP.text = nome_p
            binding.cognomeP.text = cognome_p
            binding.numeroTel.text = numero_tel.toString()
            binding.descrizione.text = descrizione
            binding.ratingBar.rating = stelle

            Log.i("esisteee", "${binding.preferita.rating}")

            if (targa != null) {
                isCarInPreferiti(targa) { exists ->
                    if (exists) {
                        binding.preferita.rating = 1F
                        Log.i("esi", "esiste")

                    }else{
                        binding.preferita.rating=0F
                    }
                }
            }
                binding.preferita.setOnRatingBarChangeListener { ratingBar, rating, b ->
                    if (rating == 1F) {
                        val query =
                            "INSERT INTO Preferiti(fk_id_utente_preferiti, fk_id_macchina_preferiti) VALUES ('${Email.getEmail()}', '$targa'); "
                        ClientNetwork.retrofit.insert(query).enqueue(
                            object : Callback<JsonObject> {
                                override fun onResponse(
                                    call: Call<JsonObject>,
                                    response: Response<JsonObject>
                                ) {
                                    binding.preferita.rating = 1F

                                }

                                override fun onFailure(
                                    call: Call<JsonObject>,
                                    t: Throwable
                                ) {
                                    TODO("Not yet implemented")

                                }

                            })

                    }


                    }
                }
















        binding.scrollView2.setOnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (view is ScrollView || view is NestedScrollView) {
                val layoutParams = binding.scrollView2.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

                binding.scrollView2.layoutParams = layoutParams
            }
        }

        binding.indietro.setOnClickListener {

            parentFragmentManager.popBackStack()

        }

        binding.prenotaButton.setOnClickListener {



            val targa = binding.targa.text.toString()


            val query =
                "INSERT INTO Prenotazione(data, orario, prezzo, recensione, segnalazione, id_macchina, id_utente) VALUES ('$data', '$orario', 00.00, 0, null, '$targa', '${Email.getEmail()}'); "
            ClientNetwork.retrofit.insert(query).enqueue(
                object : Callback<JsonObject> {
                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        if (response.isSuccessful) {
                            val query = "update Macchina set prenotazione=1 where targa= '$targa' "
                            ClientNetwork.retrofit.modifica(query).enqueue(
                                object : Callback<JsonObject> {
                                    override fun onResponse(
                                        call: Call<JsonObject>,
                                        response: Response<JsonObject>
                                    ) {
                                        if (response.isSuccessful) {

                                            val i =
                                                Intent(requireActivity(), Prenotazione::class.java)
                                            i.putExtra("email", "${Email.getEmail()}")
                                            i.putExtra("data", "$data")
                                            i.putExtra("orario", "$orario")
                                            i.putExtra("targa", "$targa")
                                            startActivity(i)


                                        } else {
                                            Log.i("errore", "erroreConnessione")
                                        }

                                    }

                                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                        TODO("Not yet implemented")
                                    }


                                })


                        } else {
                            Log.i("errore", "erroreConnessione")
                        }

                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        TODO("Not yet implemented")
                    }


                })
        }
        return binding.root
    }

    private fun isCarInPreferiti(targa: String, callback: (Boolean) -> Unit){


        val query =
            "select * from Preferiti  Where fk_id_utente_preferiti= '${Email.getEmail()}' AND fk_id_macchina_preferiti= '${targa}' ; "
        ClientNetwork.retrofit.ricerca(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        if ((response.body()?.get("queryset") as JsonArray).size() == 1) {

                            callback(true)

                        }else{
                            callback(false)
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    TODO("Not yet implemented")

                }
            })




    }

}

