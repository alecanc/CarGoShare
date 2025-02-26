package com.example.cargoshare.Profilo

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.cargoshare.Autenticazione.Email
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.R
import com.example.cargoshare.databinding.ActivityPagamentoBinding
import com.google.gson.JsonArray
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Pagamento : AppCompatActivity() {

    private lateinit var binding : ActivityPagamentoBinding
    private var numero_carta: String?=null
    private var scadenza_carta:String? = null
    private var nome_titolare:String? = null
    private var codice_carta:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityPagamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        verifyPayment { exists -> Log.i("ds", "$exists")
            if (exists) {
                binding.nomeTitolare.setText(nome_titolare)
                binding.numeroCarta.setText(numero_carta)
                binding.codiceCarta.setText(codice_carta)
                binding.dataScadenza.setText(scadenza_carta)
                binding.salvaCarta.visibility=View.GONE

                binding.nomeTitolare.isEnabled = false
                binding.numeroCarta.isEnabled = false
                binding.codiceCarta.isEnabled = false
                binding.dataScadenza.isEnabled = false
            }else{
                binding.salvaCarta.visibility=View.VISIBLE
            }

        }



        binding.backArrow.setOnClickListener {
            val intent = Intent(this, SchermataHome::class.java)
            startActivity(intent)
            finish()
        }

        binding.salvaCarta.setOnClickListener {
            insertCarta()

        }





    }

    private fun insertCarta() {



        val numero_carta = binding.numeroCarta.text.toString()
        val nome_titolare = binding.nomeTitolare.text.toString()
        val codice_carta = binding.codiceCarta.text.toString()
        val scadenza_carta = binding.dataScadenza.toString()


        val query = "update Utente set  numero_carta='${numero_carta}', scadenza_carta ='${scadenza_carta}'," +
                " nome_titolare='${nome_titolare}', codice_carta='${codice_carta}'where email='${Email.getEmail()}';"
        ClientNetwork.retrofit.insert(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                  //  lanciaCarta()
                    finish()

                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("Non funziona", t.message.toString())
                    print(t.stackTrace)
                }
            }
        )


    }


    fun lanciaCarta(){

        val view = View.inflate(this@Pagamento, R.layout.dialogpagam_view, null)

        val builder = AlertDialog.Builder(this@Pagamento)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        view.findViewById<Button>(R.id.confermaPaga).setOnClickListener {
            dialog.dismiss()

            //setto gli editText con i valori aggiunti dall'utente

            binding.nomeTitolare.setText(nome_titolare)
            binding.numeroCarta.setText(numero_carta)
            binding.codiceCarta.setText(codice_carta)
            binding.dataScadenza.setText(scadenza_carta)



            //disabilito gli editText
            binding.nomeTitolare.isEnabled = false
            binding.numeroCarta.isEnabled = false
            binding.codiceCarta.isEnabled = false
            binding.dataScadenza.isEnabled = false



        }

    }

    private fun verifyPayment(callback: (Boolean) -> Unit) {




        val query =
            "select nome_titolare, scadenza_carta, codice_carta, numero_carta from Utente where email ='${Email.getEmail()}';"
        var isPaymentVerified = false

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    if (response.isSuccessful) {
                        if ((response.body()?.get("queryset") as JsonArray).size() == 1 ) {

                            if(   ((response.body()
                                    ?.get("queryset") as JsonArray).get(0) as JsonObject).get("numero_carta")!=null &&
                                 ((response.body()
                                    ?.get("queryset") as JsonArray).get(0) as JsonObject).get("nome_titolare")!=null&&
                                    ((response.body()?.get("queryset") as JsonArray).get(0) as JsonObject).get("scadenza_carta")!=null
                                    &&((response.body()?.get("queryset") as JsonArray).get(0) as JsonObject).get("codice_carta")!=null){
                                nome_titolare=((response.body()?.get("queryset") as JsonArray).get(0) as JsonObject).get("nome_titolare").asString
                               scadenza_carta=((response.body()?.get("queryset") as JsonArray).get(0) as JsonObject).get("scadenza_carta").asString
                               codice_carta=((response.body()?.get("queryset") as JsonArray).get(0) as JsonObject).get("codice_carta").asString
                               numero_carta=((response.body()?.get("queryset") as JsonArray).get(0) as JsonObject).get("numero_carta").asString
                                isPaymentVerified = true

                            }else{
                               isPaymentVerified=false
                            }


                        }
                        callback(isPaymentVerified)
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("Ciao", t.message.toString())
                    callback(false)
                    print(t.stackTrace)
                }
            })
    }


}