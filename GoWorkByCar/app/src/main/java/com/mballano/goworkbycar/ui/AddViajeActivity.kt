package com.mballano.goworkbycar.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mballano.goworkbycar.databinding.ActivityAddViajeBinding

class AddViajeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddViajeBinding
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddViajeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val email = bundle?.getString("email")

        binding.imageButton3.setOnClickListener {
            onBackPressed()
        }

        binding.btnGuardarViaje.setOnClickListener {
            val matricula = binding.editTextMatricula.text.toString()

            db.collection("usuarios").document(email.toString())
                .collection("viajes").document(matricula).set(
                    hashMapOf("origen" to binding.editOrigen.text.toString(),
                        "destino" to binding.editDestino.text.toString(),
                        "marca" to binding.editTextMarca.text.toString(),
                        "modelo" to binding.editTextModelo.text.toString(),
                        "plazas" to binding.editTextPlazas.text.toString(),
                        "anfitrion" to email.toString())
                )

            Toast.makeText(this, "Viaje guardado con éxito", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }
}