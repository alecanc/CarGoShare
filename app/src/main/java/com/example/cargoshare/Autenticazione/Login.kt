package com.example.cargoshare.Autenticazione

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.view.View

import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.Profilo.SchermataHome
import com.example.cargoshare.databinding.ActivityLoginBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressBar:ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.loginButton.setOnClickListener {
            loginUser()
        }

        binding.backArrow.setOnClickListener {
            val intent = Intent(this, First::class.java)
            startActivity(intent)
        }



    }


    private fun loginUser() {


        val email = binding.editTextEmail.text.toString()
        val password = binding.password.text.toString()

        val query = "select * from  Utente where email ='${email}' AND password ='${password}';"
        ClientNetwork.retrofit.login(query).enqueue(
             object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {

                        if ((response.body()?.get("queryset") as JsonArray).size() == 1) {

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
                            /*     val numero_carta = ((response.body()?.get("queryset") as JsonArray).get(0)as JsonObject).get("numero_carta").asBigInteger
                                 val scadenza_carta = ((response.body()?.get("queryset") as JsonArray).get(0)as JsonObject).get("scadenza_carta").asString
                                 val nome_titolare = ((response.body()?.get("queryset") as JsonArray).get(0)as JsonObject).get("nome_titolare").asString
                                 val codice_carta = ((response.body()?.get("queryset") as JsonArray).get(0)as JsonObject).get("Codice_carta").asInt*/
                            val numero_telefonico = ((response.body()
                                ?.get("queryset") as JsonArray).get(0) as JsonObject).get("numero_telefonico").asInt
                            val email = ((response.body()
                                ?.get("queryset") as JsonArray).get(0) as JsonObject).get("email").asString



                            com.example.cargoshare.Autenticazione.Email.setEmail(email)
                            val intent = Intent(this@Login, SchermataHome::class.java)
                            intent.putExtra("nome", nome)
                            intent.putExtra("cognome", cognome)
                            intent.putExtra("indirizzo", indirizzo)
                            intent.putExtra("citta", citta)
                            intent.putExtra("cap", cap)
                            intent.putExtra("numero_telefonico", numero_telefonico)
                            intent.putExtra("email", email)
                            startActivity(intent)

                        } else {
                            Toast.makeText(
                                this@Login,
                                "Le credenziali inserite non sono corrette",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.progressBar.visibility = View.GONE
                        }
                    }


                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                    print(t.stackTrace)
                }
            }
        )


    }

}
