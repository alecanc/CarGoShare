package com.example.cargoshare.Chiave

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.cargoshare.Autenticazione.Email
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.Profilo.SchermataHome
import com.example.cargoshare.R
import com.example.cargoshare.databinding.FragmentKeyBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Key : Fragment() {
private lateinit var binding: FragmentKeyBinding
private var email:String ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentKeyBinding.inflate(layoutInflater)

        binding.sblocca.setOnClickListener {

            var dialog = DialogFragmentCarta()


            verifyPayment { exists ->
                if (exists) {
                parentFragmentManager.setFragmentResultListener("fattura", this) { requestKey, bundle ->

                }

                               val query = "select id_prenotazione, password, data, orario, id_macchina from Prenotazione Join Utente where id_utente=email AND email='${Email.getEmail()}'"
                            ClientNetwork.retrofit.ricerca(query).enqueue(
                                object : Callback<JsonObject> {
                                    override fun onResponse(
                                        call: Call<JsonObject>,
                                        response: Response<JsonObject>
                                    ) {
                                        if (response.isSuccessful) {
                                            if ((response.body()
                                                    ?.get("queryset") as JsonArray).size() >= 1) {

                                                val queryset = response.body()?.get("queryset") as JsonArray
                                                Log.i("tantotanto", "$queryset")
                                                for (i in 0 until queryset.size()) {

                                                    val mappaPrenotazioni = HashMap<Int, Triple<String, String, String>>()
                                                    mappaPrenotazioni[(queryset.get(i) as JsonObject).get("id_prenotazione").asInt]=
                                                        Triple((queryset.get(i) as JsonObject).get("data").asString,
                                                            (queryset.get(i) as JsonObject).get("orario").asString,
                                                                (queryset.get(i) as JsonObject).get("id_macchina").asString)


                                                val password = (queryset.get(0) as JsonObject).get("password").asString

                                                val text=binding.passwordPrenotazione.text.toString()
                                                if(password == text){
                                                    for (key in mappaPrenotazioni.keys) {
                                                        Log.i("dav"," m ${key.toString()}")

                                                         if(binding.key.text.toString()==key.toString()){
                                                            Bundle().apply {

                                                                val intent=
                                                                    Intent(requireContext(), SchermataHome::class.java)
                                                                intent.putExtra("email", "$email")
                                                                intent.putExtra("data", "${mappaPrenotazioni[key]?.first}")
                                                                intent.putExtra("orario", "${mappaPrenotazioni[key]?.second}")
                                                                intent.putExtra("targa", "${mappaPrenotazioni[key]?.third}")
                                                                intent.putExtra("fragment", "sblocco")
                                                                startActivity(intent)


                                                             }

                                                } else{
                                                             Toast.makeText(requireContext(), "Le password inserita non è corretta", Toast.LENGTH_LONG).show()
                                                             Log.i("pro", "scietta")
                                                }

                                            }}

                                        }

                                    }

                                }
                        }

                                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                        TODO("Not yet implemented")
                                    }
                                })


        }else {

                dialog.show(parentFragmentManager, "cartaDialog")




        }
            }
        }





      return binding.root
    }
    private fun verifyPayment(callback: (Boolean) -> Unit) {




        val query =
            "select nome_titolare, scadenza_carta, codice_carta, numero_carta from Utente where email ='${Email.getEmail()}';"
        var isPaymentVerified = false

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                            isPaymentVerified = true
                        }
                    }
                    callback(isPaymentVerified)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("Ciao", t.message.toString())
                    callback(false)
                    print(t.stackTrace)
                }
            })
    }
}