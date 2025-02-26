package com.example.cargoshare.Ricerca

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cargoshare.Autenticazione.Email
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.Profilo.SchermataHome
import com.example.cargoshare.databinding.FragmentFatturaAvvenutaBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FatturaAvvenutaFragment : AppCompatActivity() {
private var targa:String?=null
    private var recensione:Float?=null
    private var segnalazione:String?=null
    private var data:String?=null
    private var orario:String?=null
    private var email:String?=null
    private var email_p:String?=null
    private var prezzo:Float?=null
    private var durata:String?=null
    private lateinit var binding: FragmentFatturaAvvenutaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentFatturaAvvenutaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val extras: Bundle? = intent.extras
        if (extras != null) {

            targa = extras.getString("targa")
            data = extras.getString("data")
            orario = extras.getString("orario")
            email = extras.getString("email")
            prezzo=extras.getFloat("prezzo")
            durata=extras.getString("durata_corsa")

            binding.prezzo.text = prezzo.toString()
            binding.durata.text = durata


        binding.chiudi.setOnClickListener {
            recensione = binding.ratingBar.rating.toFloat()
            segnalazione = binding.segnalazione.text.toString()

            val query =
                "select email_p from Proprietario join Macchina where targa='$targa' AND id_proprietario=email_p;"
            val query2 =
                "update Prenotazione set recensione=$recensione, segnalazione='$segnalazione', prezzo=$prezzo, durata_corsa='$durata' where data='$data' AND orario='$orario' AND\n" +
                        "id_macchina= '$targa' AND id_utente= '${Email.getEmail()}'\n;" +
                        "update Proprietario set credito=$prezzo, num_stars= (select avg(recensione) from Prenotazione where email_p='$email_p';\n"
            ClientNetwork.retrofit.ricerca(query).enqueue(
                object : Callback<JsonObject> {
                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        if (response.isSuccessful) {

                            if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                                val queryset = response.body()?.get("queryset") as JsonArray
                                email_p = (queryset.get(0) as JsonObject).get("email_p").asString

                                ClientNetwork.retrofit.modifica(query2).enqueue(
                                    object : Callback<JsonObject> {
                                        override fun onResponse(
                                            call: Call<JsonObject>,
                                            response: Response<JsonObject>
                                        ) {
                                            if (response.isSuccessful) {
                                                val intent= Intent(this@FatturaAvvenutaFragment, SchermataHome::class.java)
                                                intent.putExtra("email", "$email")
                                                intent.putExtra("data", "$data")
                                                intent.putExtra("orario", "$orario")
                                                intent.putExtra("targa", "$targa")

                                                intent.putExtra("durata_corsa", "$durata")
                                                intent.putExtra("prezzo", prezzo)
                                                Log.i("durata", "$durata")
                                                startActivity(intent)


                                            }


                                        }

                                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {


                                        }

                                    })

                            }
                            }
                        }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                    }

                })


                }

        }


}


}