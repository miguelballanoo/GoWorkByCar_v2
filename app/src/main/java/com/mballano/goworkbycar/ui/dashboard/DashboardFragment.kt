package com.mballano.goworkbycar.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.mballano.goworkbycar.R
import com.mballano.goworkbycar.databinding.FragmentDashboardBinding
import com.mballano.goworkbycar.ui.MdfDatosActivity
import com.mballano.goworkbycar.ui.MisViajesActivity
import com.mballano.goworkbycar.ui.MisViajesCreadosActivity

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val bundle = activity?.intent?.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        binding.btnMisViajesCreados.setOnClickListener {
            val misViajesIntent = Intent(requireContext(), MisViajesCreadosActivity::class.java).apply {
                putExtra("email", email)
            }
            startActivity(misViajesIntent)
        }

        binding.btnMisViajesReservados.setOnClickListener {
            val misViajesIntent = Intent(requireContext(), MisViajesActivity::class.java).apply {
                putExtra("email", email)
            }
            startActivity(misViajesIntent)
        }

        binding.btnMdfDatos.setOnClickListener {

            val mdfDatosIntent = Intent(requireContext(), MdfDatosActivity::class.java).apply{
                putExtra("email", email)
                putExtra("provider", provider)
            }
            startActivity(mdfDatosIntent)
        }

        binding.btnCerrarSesion.setOnClickListener {

            //Setup
            setup(email ?: "", provider ?: "")

            //Guardado de datos

            val prefs= activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)?.edit()
            prefs?.putString("email", email)
            prefs?.putString("provider", provider)
            prefs?.apply()
        }


    }
    private fun setup(s: String, s1: String) {

        val prefs= activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
            ?.edit()
        if (prefs != null) {
            prefs.clear()
        }
        if (prefs != null) {
            prefs.apply()
        }

        FirebaseAuth.getInstance().signOut()
        activity?.onBackPressed()
        activity?.onBackPressed()
    }
}