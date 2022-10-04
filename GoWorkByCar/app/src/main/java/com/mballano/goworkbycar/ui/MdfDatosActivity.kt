package com.mballano.goworkbycar.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mballano.goworkbycar.MainActivity
import com.mballano.goworkbycar.databinding.ActivityMdfDatosBinding

class MdfDatosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMdfDatosBinding
    private var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMdfDatosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        binding.txtEmail.text = email.toString()

        //Se pintan los datos que ya tiene el usuario en casso de que este ya lo haya modificado
        db.collection("usuarios").document(email.toString()).get()
            .addOnSuccessListener {
                if(it.get("nombre")!=null && it.get("telefono")!= null && it.get("direccion")!=null && it.get("edad")!=null){
                    binding.editTextTextPersonName.setText(it.get("nombre") as String)
                    binding.editTlf.setText(it.get("telefono") as String)
                    binding.editDireccion.setText(it.get("direccion") as String)
                    binding.editTextPersonaEdad.setText(it.get("edad") as String)
                }

            }

        binding.btnGuardar.setOnClickListener {

            if(binding.editTextTextPersonName.text.isNotEmpty() || binding.editDireccion.text.isNotEmpty() ||
                    binding.editTlf.text.isNotEmpty() || binding.editTlf.text.isNotEmpty()){
                db.collection("usuarios").document(email.toString()).set(
                    hashMapOf("provider" to provider,
                        "nombre" to binding.editTextTextPersonName.text.toString(),
                        "direccion" to binding.editDireccion.text.toString(),
                        "telefono" to binding.editTlf.text.toString(),
                        "edad" to binding.editTextPersonaEdad.text.toString())
                ).addOnCompleteListener {
                    Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show()
                    val homeIntent = Intent(this, MainActivity::class.java).apply{
                        putExtra("email", email)
                        putExtra("provider", provider)
                    }
                    startActivity(homeIntent)
                }
            }else{
                Toast.makeText(this, "Debes de rellenar todos los campos", Toast.LENGTH_SHORT).show()
            }

        }
    }
}