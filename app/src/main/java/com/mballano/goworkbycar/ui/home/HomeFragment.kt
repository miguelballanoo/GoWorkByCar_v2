package com.mballano.goworkbycar.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.toObject
import com.mballano.goworkbycar.MainActivity
import com.mballano.goworkbycar.databinding.FragmentHomeBinding
import com.mballano.goworkbycar.ui.*
import java.util.*

class HomeFragment : Fragment(){

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viajesArrayList: ArrayList<ViajeModel>
    private lateinit var viajesAdapter: ViajesAdapter
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = activity?.intent?.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        binding.btnAddViaje.setOnClickListener {
            val addViajeIntent = Intent(requireContext(), AddViajeActivity::class.java).apply{
                putExtra("email", email)
                putExtra("provider", provider)
            }
            startActivity(addViajeIntent)
        }


        getViajes()
    }

    private fun getViajes() {
        binding.rvViajes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvViajes.setHasFixedSize(true)

        viajesArrayList = arrayListOf()

        val bundle = activity?.intent?.extras
        val email = bundle?.getString("email")
        viajesAdapter = email?.let { ViajesAdapter(viajesArrayList, it) }!!
        binding.rvViajes.adapter = viajesAdapter

        if (email != null) {
            eventChangeListener(email)
        }
    }

    private fun eventChangeListener(email: String) {


        db = FirebaseFirestore.getInstance()
        //Busqueda de todos los usuarios
        db.collection("usuarios").
        addSnapshotListener(object :EventListener<QuerySnapshot>{
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
                        db.collection("usuarios").document(dc.document.id).collection("viajes").
                        addSnapshotListener(object : EventListener<QuerySnapshot> {
                            override fun onEvent(
                                value: QuerySnapshot?,
                                error: FirebaseFirestoreException?
                            ) {
                                if(error!=null){
                                    Log.e("Firestore error", error.message.toString())
                                    return
                                }
                                for(dc1: DocumentChange in value?.documentChanges!!){
                                       if(dc1.type == DocumentChange.Type.ADDED){
                                           val viaje: ViajeModel = dc1.document.toObject(ViajeModel::class.java)
                                           viaje.matricula = dc1.document.id
                                           viajesArrayList.add(viaje)
                                       }
                                }
                                viajesAdapter.notifyDataSetChanged()
                            }

                        })

                    }
                }
            }

        })
    }
}
