package com.mballano.goworkbycar

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mballano.goworkbycar.databinding.ActivityAuthBinding
import com.mballano.goworkbycar.ui.MdfDatosActivity

enum class ProviderType{
    BASIC,
    GOOGLE,
    FALSE
}
class AuthActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityAuthBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setup
        setup()
        session()

    }

    override fun onStart() {
        super.onStart()
        binding.authLayout.visibility = View.VISIBLE

    }
    private fun session() {
        val prefs= getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if(email!=null && provider!=null){
            binding.authLayout.visibility = View.INVISIBLE
            showMenu(email, ProviderType.valueOf(provider))
        }
    }

    private fun setup() {
        title="Autenticacion"
        binding.signUpBtn.setOnClickListener {
            if(binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()).addOnCompleteListener {
                        if(it.isSuccessful){
                            val homeIntent = Intent(this, MdfDatosActivity::class.java).apply{
                                putExtra("email", it.result?.user?.email)
                                putExtra("provider", ProviderType.BASIC.name)
                            }
                            startActivity(homeIntent)
                        }else{
                            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        binding.loginBtn.setOnClickListener {
            if(binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        showMenu(it.result?.user?.email ?: "", ProviderType.BASIC)
                    }else{
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.googleBtn.setOnClickListener {
            //Configuracion
            auth = Firebase.auth
            auth.signOut()

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("216029831110-d0qqt8v2bjnc707d3ge7uc70ivp1vphu.apps.googleusercontent.com")
                .requestEmail()
                .build()
            GoogleSignIn.getClient(this, gso).signOut()
            googleSignInClient = GoogleSignIn.getClient(this, gso)
            signIn()

        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val GOOGLE_SIGN_IN = 100
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showMenu(account.email ?: "", ProviderType.GOOGLE)
                    } else {
                        Toast.makeText(this, "Error en onActivityResult", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Error en onActivityResult", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showMenu(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, MainActivity::class.java).apply{
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }


}