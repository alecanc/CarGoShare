package com.example.cargoshare.Profilo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cargoshare.Prenotazioni.Adapter
import com.example.cargoshare.Prenotazioni.Prenotazione
import com.example.cargoshare.databinding.CardFattureBinding

class AdapterFatture(private val mList: ArrayList<Fattura>) : RecyclerView.Adapter<AdapterFatture.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = CardFattureBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Fattura = mList[position]

        holder.dataView.text = Fattura.data
        holder.prezzoView.text = Fattura.prezzo
    }



    override fun getItemCount(): Int {

        return mList.size
    }


    class ViewHolder(binding: CardFattureBinding) : RecyclerView.ViewHolder(binding.root) {



        val dataView = binding.data
        val prezzoView = binding.prezzo

    }

}