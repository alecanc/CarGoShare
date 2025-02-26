package com.example.cargoshare.Chiave


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.cargoshare.Autenticazione.Email
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.R
import com.example.cargoshare.databinding.FragmentDialogCartaBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class DialogFragmentCarta : DialogFragment() {


    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentDialogCartaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var cartaView :View = inflater.inflate(R.layout.fragment_dialog_carta, container, false)

        binding.inserisciCarta.setOnClickListener {
            dismiss()
            val numero_carta = binding.editnumeroCarta.text.toString()
            val nome_titolare = binding.editnomeTitolare.text.toString()
            val codice_carta = binding.editcodiceCarta.text.toString()
            val scadenza_carta = binding.editdataScadenza.toString()


            val query = "update Utente set  numero_carta='${numero_carta}', scadenza_carta ='${scadenza_carta}'," +
                    " nome_titolare='${nome_titolare}', codice_carta='${codice_carta}'where email='${Email.getEmail()}';"
            ClientNetwork.retrofit.insert(query).enqueue(
                object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {


                    }
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Log.i("Non funziona", t.message.toString())
                        print(t.stackTrace)
                    }
                }
            )



        }
        return cartaView

    }





}