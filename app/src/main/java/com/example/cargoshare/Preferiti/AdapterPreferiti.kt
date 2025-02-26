package com.example.cargoshare.Preferiti

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cargoshare.databinding.CardPreferitiBinding

class AdapterPreferiti(private val mList: ArrayList<Preferito>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<AdapterPreferiti.ViewHolder>() {

    interface OnItemClickListener {
        fun onButtonClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = CardPreferitiBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(view, itemClickListener)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val Preferito = mList[position]

        holder.marcaView.text = Preferito.marca
        holder.modelloView.text = Preferito.modello
        holder.postiView.text = Preferito.posti
        holder.tipologiaView.text = Preferito.tipologia
        holder.indirizzoView.text = Preferito.indirizzo
holder.targa.text= Preferito.targa
        holder.annoView.text = Preferito.anno
        holder.tariffaView.text = Preferito.tariffa
          holder.cambioView.text = Preferito.cambio
          holder.stellaView.setOnRatingBarChangeListener{ _, rating, _ ->
Log.i("rat", "$rating")
              if (rating == 1f) {
                  Log.i("rat", "rat seleziona $rating")

                  Log.i("rat", "rat sele $holder.stellaView.rating")
                  itemClickListener.onButtonClick(position)
              }
        }

    }



    override fun getItemCount(): Int {

        return mList.size
    }


    class ViewHolder(binding: CardPreferitiBinding, private val itemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(binding.root) {



val targa= binding.targa
        val postiView = binding.numPosti
        val cambioView = binding.cambio
        val modelloView = binding.modello
        val tipologiaView = binding.tipologia
        val indirizzoView = binding.indirizzo
        val marcaView = binding.marca
        val annoView = binding.anno
        val tariffaView = binding.tariffa
        val stellaView=binding.stella

        init {
            binding.stella.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onButtonClick(position)
                }
            }
        }
    }
}