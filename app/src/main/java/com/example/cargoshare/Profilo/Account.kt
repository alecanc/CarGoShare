package com.example.cargoshare.Profilo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cargoshare.Autenticazione.Login
import com.example.cargoshare.R
import com.example.cargoshare.databinding.FragmentAccountBinding
import com.google.android.material.navigation.NavigationView


class Account : Fragment() {

    private lateinit var binding : FragmentAccountBinding
    private lateinit var navMenu : NavigationView




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

              parentFragmentManager.setFragmentResultListener("prova",this){
                      prova,bundle->
                  val result = bundle.getBundle("ciao")
                  Log.i("prova","${result?.getString("nome")} , ${result?.getString("cognome")} ",)
                  binding.nomeCognome.text = "${result?.getString("nome")} ${result?.getString("cognome")}" }


            binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
            navMenu = binding.navigationView
            navMenu.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.navigation_user -> {

                        (requireActivity() as SchermataHome).lanciaActivity()
                        true
                    }

                    R.id.navigation_about-> {
                        val intent = Intent(requireActivity(), About::class.java)
                        startActivity(intent)
                        true
                    }

                    R.id.navigation_fatture -> {
                        val intent = Intent(requireActivity(), Fatture::class.java)
                        startActivity(intent)
                        true
                    }

                    R.id.navigation_pagamento -> {
                        val intent = Intent(requireActivity(), Pagamento::class.java)
                        startActivity(intent)
                        true
                    }

                    R.id.navigation_settings -> {
                        (requireActivity() as SchermataHome).lanciaSettings()

                        true
                    }

                    R.id.logout -> {
                        val intent = Intent(requireActivity(), Login::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        true
                    }

                    else -> false
                }
            }


            return binding.root

        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragmentManager.setFragmentResultListener("prova",this){
                prova,bundle->
            val result = bundle.getBundle("ciao")
            Log.i("prova","${result?.getString("nome")} , ${result?.getString("cognome")} ",)
            binding.nomeCognome.text = "${result?.getString("nome")} ${result?.getString("cognome")}" }


    }



    }
