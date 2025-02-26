package com.example.cargoshare.Ricerca

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.databinding.CardCarBinding
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdapterCar(private val mList: ArrayList<Car>, ) : RecyclerView.Adapter<AdapterCar.ViewHolder>() {
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = CardCarBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Macchina = mList[position]





        holder.marcaView.text = Macchina.marca
        holder.modelloView.text = Macchina.modello
        holder.postiView.text = Macchina.posti
        holder.tipologiaView.text = Macchina.tipologia
        holder.indirizzoView.text = Macchina.indirizzo

        holder.annoView.text = Macchina.anno
        holder.tariffaView.text = Macchina.tariffa
        holder.cambioView.text = Macchina.cambio



    }



    override fun getItemCount(): Int {

        return mList.size
    }


    class ViewHolder(binding: CardCarBinding, listener: onItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {



        val postiView = binding.numPosti
        val cambioView = binding.cambio
        val modelloView = binding.modello
        val tipologiaView = binding.tipologia
        val indirizzoView = binding.indirizzo
        val marcaView = binding.marca
        val annoView = binding.anno
        val tariffaView = binding.tariffa


        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }
}

