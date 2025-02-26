package com.example.cargoshare.Profilo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cargoshare.R
import com.example.cargoshare.databinding.ListItemBinding


class CustomAdapter(private val dataSet: List<Domande>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(domanda: String, risposta: String) {
            binding.questionTextView.text = domanda
            binding.answerTextView.text = risposta
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val domandaRisposta = dataSet[position]
        holder.bind(domandaRisposta.domanda, domandaRisposta.risposta)

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}