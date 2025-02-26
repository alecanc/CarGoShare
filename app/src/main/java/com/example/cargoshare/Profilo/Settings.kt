package com.example.cargoshare.Profilo

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import com.example.cargoshare.Autenticazione.Email
import com.example.cargoshare.Autenticazione.Login
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.R
import com.example.cargoshare.databinding.ActivitySettingsBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Settings : AppCompatActivity() {



    private lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)





        binding.salvaButton.setOnClickListener {
            modificaUser()

        }

         binding.backArrow.setOnClickListener {
            val intent = Intent(this, SchermataHome::class.java)
            startActivity(intent)
            finish()
        }



    }





    private fun modificaUser() {



        val nome = binding.editName.text.toString()
        val cognome = binding.editSurname.text.toString()
        val password = binding.editTextPassword.text.toString()
        val indirizzo = binding.editIndirizzo.text.toString()
        val citta = binding.editCitta.text.toString()
        val cap = binding.editCap.text.toString()
        val numero_telefonico = binding.editNumber.text?.toString()

        val query = "update Utente set  nome='${nome}', password ='${password}',cognome='${cognome}', indirizzo='${indirizzo}', citta='${citta}', cap=${cap}, numero_telefonico=${numero_telefonico} where email='${Email.getEmail()}';"
        ClientNetwork.retrofit.modifica(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    println("funziona l'update settings")
                    lanciaModificati()

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("Non funziona la query per settings", t.message.toString())
                    print(t.stackTrace)
                }
            }
        )


    }

    fun lanciaModificati(){

        val view = View.inflate(this@Settings, R.layout.dialogmod_view, null)

        val builder = AlertDialog.Builder(this@Settings)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        view.findViewById<Button>(R.id.torna).setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, Profilo::class.java)
            startActivity(intent)


        }


            }


    }

