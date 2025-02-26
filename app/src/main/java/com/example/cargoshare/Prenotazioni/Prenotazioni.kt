package com.example.cargoshare.Prenotazioni

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cargoshare.Autenticazione.Email
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.databinding.FragmentPrenotazioniBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Prenotazioni : Fragment() {
 private lateinit var binding: FragmentPrenotazioniBinding
private lateinit var adapter :Adapter
val data= ArrayList<Prenotazione>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPrenotazioniBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = Adapter(data, object : Adapter.OnItemClickListener {
            override fun onButtonClick(position: Int) {
                val deletedItem = data[position]
                data.removeAt(position)
                adapter.notifyItemRemoved(position)
                deleteItemFromDatabase(deletedItem.id)
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = adapter
        getInfo()
    }



    private fun getInfo() {


        val query =
            " select data, modello,orario, marca,anno,foto, id_prenotazione from Macchina JOIN Prenotazione where id_macchina=targa AND prezzo= 0.0 AND id_utente='${Email.getEmail()}';"
        ClientNetwork.retrofit.ricerca(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        if ((response.body()?.get("queryset") as JsonArray).size() >= 1) {
                            val queryset = response.body()?.get("queryset") as JsonArray

                            for (i in 0 until queryset.size()) {

                                data.add(
                                    Prenotazione(

                                        (queryset.get(i) as JsonObject).get("data").asString,
                                        (queryset.get(i) as JsonObject).get("marca").asString,
                                        (queryset.get(i) as JsonObject).get("modello").asString,
                                        (queryset.get(i) as JsonObject).get("anno").asString,
                                        (queryset.get(i) as JsonObject).get("id_prenotazione").asInt,
                                        (queryset.get(i) as JsonObject).get("orario").asString
                                    )
                                )


                                binding.recyclerview.adapter = adapter
                                val layoutManager = LinearLayoutManager(requireContext())
                                binding.recyclerview.layoutManager = layoutManager

                            }
                            }else {
                                Toast.makeText(
                                    requireContext(),
                                    "Non ci sono prenotazioni in corso",
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                            }else{
                            Toast.makeText(
                                requireContext(),
                                "Non ci sono prenotazioni in corso",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }




            }





    private fun deleteItemFromDatabase(deletedItem: Int) {
        val query="delete from Prenotazione where id_prenotazione=$deletedItem;"
        ClientNetwork.retrofit.remove(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {


                }
            }
        )



    }

