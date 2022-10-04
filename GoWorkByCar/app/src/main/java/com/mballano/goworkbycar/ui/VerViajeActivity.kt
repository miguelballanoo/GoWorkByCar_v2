package com.mballano.goworkbycar.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.mballano.goworkbycar.databinding.ActivityVerViajeBinding
import java.util.ArrayList

class VerViajeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerViajeBinding
    private lateinit var db : FirebaseFirestore
    private lateinit var usuariosArrayList: ArrayList<UsuarioModel>
    private lateinit var usuariosAdapter: UsuariosAdapter
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerViajeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()


        //Relleno de datos del viaje
        val viaje: ViajeModel = intent.getSerializableExtra("viaje") as ViajeModel//Captura del viaje
        val email: String? = intent.getStringExtra("email")
        binding.tvMarca.text = viaje.marca
        binding.tvModelo.text = viaje.modelo
        binding.tvOrigen.text = viaje.origen
        binding.tvDestino.text = viaje.destino
        binding.tvPlazas.text = viaje.plazas
        binding.tvMatricula.text = viaje.matricula
        id = viaje.id


        //Reserva de viaje
        binding.btnReservarViaje.setOnClickListener {


            var encontrado: Boolean = false
            for(e in viaje.participantes){
                if(e == email){
                    encontrado = true
                }
            }

var id: String = ""
            if(!encontrado){
                if(viaje.plazas.toInt()>0){
                    if (email != null) {
                        viaje.participantes.add(email)//Añadido del email del usuario a la lista de participantes del viaje
                    }
                    //Reduccion de plazas del viaje
                    val updatePlazas= viaje.plazas.toInt()-1
                    viaje.plazas = updatePlazas.toString()
                    binding.tvPlazas.setText(viaje.plazas)


                    //Añadido del viaje a la coleccion de misViajes (viajes reservados)
                    if (email != null) {
                        db.collection("usuarios")
                            .document(email)
                            .collection("misViajes").add(viaje)

                        //Modificación de las plazas del viaje en la base de datos
                        db.collection("usuarios")
                            .document(viaje.anfitrion)
                            .collection("viajes")
                            .document(viaje.matricula).update("plazas", updatePlazas.toString())



                    }

                }else{
                    Toast.makeText(this@VerViajeActivity, "Plazas agotadas", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this@VerViajeActivity, "Error: Ya se ha reservado el viaje", Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnCancearViaje.setOnClickListener {
            /*
            //Quitar del array de participantes del viaje el usuario
            for (i in viaje.participantes){
                if(email == i){
                    viaje.participantes.remove(i)
                    viaje.plazas = (viaje.plazas.toInt()+1).toString()
                }
            }
            //Quitar de la coleccion misViajes del usuario el viaje seleccionado
            db.collection("usuarios")
                .document(viaje.anfitrion)
                .collection("misViajes").document(viaje.id).delete()*/
        }


        /*Boton de retroceso*/
        binding.imageButton3.setOnClickListener {
            onBackPressed()
        }


    }
}