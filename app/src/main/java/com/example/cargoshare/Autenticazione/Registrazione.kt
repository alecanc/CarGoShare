package com.example.cargoshare.Autenticazione

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.Profilo.Profilo
import com.example.cargoshare.databinding.ActivityRegistrazioneBinding
import com.example.cargoshare.Profilo.SchermataHome
import com.example.cargoshare.R
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registrazione : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrazioneBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrazioneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.salvaButton.setOnClickListener{
            registerUser()

        }


        binding.backArrow.setOnClickListener {
            onBackPressed()
        }



    }


    private fun registerUser() {


        val nome = binding.registerNome.text.toString()
        val cognome = binding.registerCognome.text.toString()
        val email = binding.registerEmail.text.toString()
        val indirizzo = binding.registerAddress.text.toString()
        val citta = binding.registerCitta.text.toString()
        val cap = binding.registerCap.text.toString()
        val password = binding.registerPassword.text.toString()
        val numero_telefonico = binding.registerNumero.text.toString()


        val query = "insert into  Utente(nome, cognome, email, indirizzo, citta, cap, password, numero_telefonico, foto) values ('${nome}', '${cognome}', '${email}','${indirizzo}', '${citta}', ${cap}, '${password}', ${numero_telefonico}, 'media/images/utenti/avatar.jpg');"
        ClientNetwork.retrofit.insert(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    lanciaDialog()

                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("Non funziona", t.message.toString())
                    print(t.stackTrace)
                }
            }
        )


    }


    fun lanciaDialog(){

        val view = View.inflate(this, R.layout.dialog_view, null)

        val builder = AlertDialog.Builder(this)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

       view.findViewById<Button>(R.id.inserisci).setOnClickListener {
           dialog.dismiss()
           val intent = Intent(this, CartaPagamento ::class.java)
           startActivity(intent)
       }





    }




}