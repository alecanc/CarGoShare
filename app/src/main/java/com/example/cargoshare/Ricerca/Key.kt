package com.example.cargoshare.Ricerca



import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.cargoshare.Autenticazione.Email
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.Profilo.SchermataHome
import com.example.cargoshare.R
import com.example.cargoshare.databinding.ActivitySbloccaOraBinding
import com.example.cargoshare.databinding.ActivitySettingsBinding

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Key : AppCompatActivity() {


    private lateinit var binding: ActivitySbloccaOraBinding


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivitySbloccaOraBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.sbloccaButton2.setOnClickListener {

           verifyPayment { exists ->
                    if (exists) {

                        val extras: Bundle? = intent.extras
                        if (extras != null) {
                            val email = extras.getString("email")
                            val targa = extras.getString("targa")
                            val data = extras.getString("data")
                            val orario = extras.getString("orario")

                            Log.i("sta", "vediiii '$email'")
                            Log.i("prova2", "$orario")
                            Log.i("prova2", "$targa")
                            Log.i("prova2", "$data")

                            val query = "SELECT password FROM Utente Where email= '$email';"
                            ClientNetwork.retrofit.ricerca(query).enqueue(
                                object : Callback<JsonObject> {
                                    override fun onResponse(
                                        call: Call<JsonObject>,
                                        response: Response<JsonObject>
                                    ) {
                                        if (response.isSuccessful) {
                                            if ((response.body()
                                                    ?.get("queryset") as JsonArray).size() == 1
                                            ) {

                                                val queryset =
                                                    response.body()?.get("queryset") as JsonArray
                                                val password =
                                                    (queryset.get(0) as JsonObject).get("password").asString

                                                val text = binding.passwordText.text.toString()

                                                if (password == text) {

                                                    val intent =
                                                        Intent(this@Key, SchermataHome::class.java)
                                                    intent.putExtra("email", "$email")
                                                    intent.putExtra("data", "$data")
                                                    intent.putExtra("orario", "$orario")
                                                    intent.putExtra("targa", "$targa")
                                                    intent.putExtra("fragment", "sblocco")
                                                    startActivity(intent)


                                                } else {
                                                    Log.i("pro", "cietta")
                                                    //TODO password errata
                                                }

                                            }

                                        }

                                    }

                                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                        TODO("Not yet implemented")
                                    }
                                })
                        }

                    } else {
                        val view = View.inflate(this@Key,  R.layout.activity_dialog_carta, null)

                        val builder = AlertDialog.Builder(this@Key)
                        builder.setView(view)

                        val dialog = builder.create()
                        dialog.show()
                        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                        view.findViewById<Button>(R.id.inserisciCarta).setOnClickListener {
                            dialog.dismiss()
                        }

                    }
                }
        }
    }
    }

    private fun verifyPayment(callback: (Boolean) -> Unit) {
        val query = "select nome_titolare, scadenza_carta, codice_carta, numero_carta from Utente where email ='${Email.getEmail()}';"
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

                    callback(false)
                    print(t.stackTrace)
                }
            })
    }


