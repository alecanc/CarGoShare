package com.example.cargoshare.Ricerca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cargoshare.Autenticazione.Email
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.R
import com.example.cargoshare.databinding.ActivityDialogCartaBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DialogCarta : AppCompatActivity() {

    private lateinit var binding : ActivityDialogCartaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_carta)

        binding.inserisciCarta.setOnClickListener {
            val numero_carta = binding.editnumeroCarta.text.toString()
            val nome_titolare = binding.editnomeTitolare.text.toString()
            val codice_carta = binding.editcodiceCarta.text.toString()
            val scadenza_carta = binding.editdataScadenza.toString()


            val query = "update Utente set  numero_carta='${numero_carta}', scadenza_carta ='${scadenza_carta}'," +
                    " nome_titolare='${nome_titolare}', codice_carta='${codice_carta}'where email='${Email.getEmail()}';"
            ClientNetwork.retrofit.insert(query).enqueue(
                object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        val intent = Intent(this@DialogCarta, CorsaAvviataFrag::class.java)
                        startActivity(intent)

                    }
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                    }
                }
            )

        }



    }
}