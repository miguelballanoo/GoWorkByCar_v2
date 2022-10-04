package com.mballano.goworkbycar.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mballano.goworkbycar.R

class UsuariosAdapter(val listaUsuarios: ArrayList<UsuarioModel>): RecyclerView.Adapter<UsuariosAdapter.UsuariosViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsuariosAdapter.UsuariosViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_usuario,parent, false)
        return UsuariosViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UsuariosAdapter.UsuariosViewHolder, position: Int) {
        val usuario : UsuarioModel = listaUsuarios[position]
        holder.name.text = usuario.nombre
    }

    override fun getItemCount(): Int {
        return listaUsuarios.size
    }

    public class UsuariosViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.tv1)
    }

}
