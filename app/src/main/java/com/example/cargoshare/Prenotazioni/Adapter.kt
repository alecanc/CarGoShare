package com.example.cargoshare.Prenotazioni


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cargoshare.databinding.CardPrenotazioneBinding



class Adapter(private val mList: ArrayList<Prenotazione>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<Adapter.ViewHolder>() {


    interface OnItemClickListener {
        fun onButtonClick(position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = CardPrenotazioneBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(view,itemClickListener)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val Prenotazione = mList[position]


        holder.dataView.text = Prenotazione.data
        holder.annoView.text = Prenotazione.anno
        holder.marcaView.text= Prenotazione.marca
        holder.modelloView.text=Prenotazione.modello
holder.idView.text="ID:  "+Prenotazione.id.toString()
        holder.orarioView.text= Prenotazione.orario
        holder.annulla.setOnClickListener {
            itemClickListener.onButtonClick(position)
        }

    }


    override fun getItemCount(): Int {

        return mList.size
    }


    class ViewHolder(binding: CardPrenotazioneBinding, private val itemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(binding.root) {


        val dataView = binding.data
        val annoView = binding.anno
        val marcaView = binding.marca
        val modelloView = binding.modello
        val idView = binding.idPrenotazione
        val orarioView= binding.orario
        val annulla = binding.annulla

        init {


            binding.annulla.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onButtonClick(position)

                }

            }
        }
    }

}