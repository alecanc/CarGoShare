package com.example.cargoshare.Preferiti

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cargoshare.Autenticazione.Email
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.R
import com.example.cargoshare.databinding.FragmentPreferitiBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Preferiti : Fragment() {

    private lateinit var binding: FragmentPreferitiBinding
    private lateinit var adapter: AdapterPreferiti
    val data = ArrayList<Preferito>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreferitiBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AdapterPreferiti(data, object : AdapterPreferiti.OnItemClickListener {
            override fun onButtonClick(position: Int) {
                val unselectedItem = data[position]

                data.removeAt(position)
                adapter.notifyItemRemoved(position)
                deleteItemFromDatabase(unselectedItem.targa)
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = adapter
        caricaPreferiti()

    }


    private fun caricaPreferiti() {
        val query =
            " select  num_posti, indirizzo, tipologia, marca, modello, cambio, tariffa, targa, anno\n" +
                    " from Preferiti JOIN Macchina Where fk_id_macchina_preferiti=targa AND fk_id_utente_preferiti='${Email.getEmail()}';"
        ClientNetwork.retrofit.ricerca(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        if ((response.body()?.get("queryset") as JsonArray).size() >= 1) {
                            val queryset = response.body()?.get("queryset") as JsonArray

                            for (i in 0 until queryset.size()) {
                                data.add(
                                    Preferito(
                                        (queryset.get(i) as JsonObject).get("cambio").asString,
                                        (queryset.get(i) as JsonObject).get("num_posti").asString,
                                        (queryset.get(i) as JsonObject).get("indirizzo").asString,
                                        (queryset.get(i) as JsonObject).get("tipologia").asString,
                                        (queryset.get(i) as JsonObject).get("tariffa").asString,
                                        (queryset.get(i) as JsonObject).get("marca").asString,
                                        (queryset.get(i) as JsonObject).get("modello").asString,
                                        (queryset.get(i) as JsonObject).get("anno").asString,
                                        (queryset.get(i) as JsonObject).get("targa").asString
                                    )
                                )


                                binding.recyclerview.adapter = adapter
                                val layoutManager = LinearLayoutManager(requireContext())
                                binding.recyclerview.layoutManager = layoutManager

                            }

                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Non ci sono preferiti",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Non ci sono preferiti",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })

    }


    private fun deleteItemFromDatabase(deletedItem: String) {
        val query =
            "delete from Preferiti where fk_id_utente_preferiti='${Email.getEmail()}'  AND fk_id_macchina_preferiti='$deletedItem';"

        ClientNetwork.retrofit.remove(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Errore nella risposta:",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    TODO("Not yet implemented")
                }


            }
        )


    }

}





