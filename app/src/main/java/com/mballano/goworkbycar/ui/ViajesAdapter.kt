package com.mballano.goworkbycar.ui

import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mballano.goworkbycar.R
import java.io.Serializable

class ViajesAdapter(
    val listaViajes: ArrayList<ViajeModel>,
    val email: String
):
    RecyclerView.Adapter<ViajesAdapter.ViajesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViajesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_viaje,parent, false)
        return ViajesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViajesViewHolder, position: Int) {
        val viaje : ViajeModel = listaViajes[position]

        holder.origen.text = viaje.origen
        holder.destino.text = viaje.destino
        holder.plazas.text = viaje.plazas


        holder.itemView.setOnClickListener {
            val VerViajentent = Intent(holder.itemView.context, VerViajeActivity::class.java).apply{
                putExtra("viaje", viaje as Serializable)
                putExtra("email", email)
            }
            holder.itemView.context.startActivity(VerViajentent)
        }
    }

    override fun getItemCount(): Int = listaViajes.size

    inner class ViajesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val origen: TextView = itemView.findViewById(R.id.tvOrigen)
        val destino: TextView = itemView.findViewById(R.id.tvDestino)
        val plazas: TextView = itemView.findViewById(R.id.tvPlazas)
    }

}
