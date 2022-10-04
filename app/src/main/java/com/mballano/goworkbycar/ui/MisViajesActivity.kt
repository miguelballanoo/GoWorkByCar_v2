package com.mballano.goworkbycar.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.mballano.goworkbycar.R
import com.mballano.goworkbycar.databinding.ActivityMisViajesBinding
import com.mballano.goworkbycar.databinding.ActivityVerViajeBinding
import java.util.ArrayList

class MisViajesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMisViajesBinding
    private lateinit var viajesArrayList: ArrayList<ViajeModel>
    private lateinit var viajesAdapter: ViajesAdapter
    private lateinit var db : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMisViajesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvViajes.layoutManager = LinearLayoutManager(this)
        binding.rvViajes.setHasFixedSize(true)

        viajesArrayList = arrayListOf()

        val bundle = intent?.extras
        val email = bundle?.getString("email")
        viajesAdapter = email?.let { ViajesAdapter(viajesArrayList, it) }!!
        binding.rvViajes.adapter = viajesAdapter

        eventChangeListener(email)
    }

    private fun eventChangeListener(email: String) {

        db = FirebaseFirestore.getInstance()
        //Busqueda de todos los usuarios
        db.collection("usuarios").document(email).collection("misViajes").
        addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ) {
                if(error!=null){
                    Log.e("Firestore error", error.message.toString())
                    return
                }
                for(dc: DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.ADDED){
                        var viaje: ViajeModel = dc.document.toObject(ViajeModel::class.java)
                        viaje.matricula = dc.document.id
                        //viaje.anfitrion = anfitrion
                        viajesArrayList.add(viaje)
                    }
                }
                viajesAdapter.notifyDataSetChanged()
            }

        })
    }
}