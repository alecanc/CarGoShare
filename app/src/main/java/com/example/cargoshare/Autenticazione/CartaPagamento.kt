package com.example.cargoshare.Autenticazione

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.cargoshare.Autenticazione.Login
import com.example.cargoshare.Autenticazione.Registrazione
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.Profilo.SchermataHome
import com.example.cargoshare.R
import com.example.cargoshare.databinding.ActivityCartaPagamentoBinding
import com.example.cargoshare.databinding.ActivityPagamentoBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartaPagamento : AppCompatActivity() {

    private lateinit var binding : ActivityCartaPagamentoBinding
    private var numero_carta: Int = 0
    private var scadenza_carta:String? = null
    private var nome_titolare:String? = null
    private var codice_carta:Int= 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityCartaPagamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener {
            val intent = Intent(this, Registrazione::class.java)
            startActivity(intent)
        }

        binding.salvaCarta.setOnClickListener {
            insertCarta()

        }

        val extras: Bundle? = intent.extras
        if (extras != null) {
            nome_titolare = extras.getString("nome")
            scadenza_carta = extras.getString("cognome")
            codice_carta = extras.getInt("codice_carta")
            numero_carta = extras.getInt("numero_carta")

        }

    }


    private fun insertCarta() {




        val numero_carta = binding.numeroCarta.text.toString()
        val nome_titolare = binding.nomeTitolare.text.toString()
        val codice_carta = binding.codiceCarta.text.toString()
        val scadenza_carta = binding.dataScadenza.toString()


        val query = "update Utente set  numero_carta=${numero_carta}, scadenza_carta ='${scadenza_carta}'," +
                " nome_titolare='${nome_titolare}', codice_carta=${codice_carta} where email='${Email.getEmail()}';"
        ClientNetwork.retrofit.insert(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    lanciaCarta1()

                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("Non funziona", t.message.toString())
                    print(t.stackTrace)
                }
            }
        )


    }


    fun lanciaCarta1(){

        val view = View.inflate(this@CartaPagamento, R.layout.dialogpagam_view, null)

        val builder = AlertDialog.Builder(this@CartaPagamento)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        view.findViewById<Button>(R.id.confermaPaga).setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)

            finish()



        }

    }



}