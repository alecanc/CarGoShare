package com.example.cargoshare.Profilo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cargoshare.Chiave.Key
import com.example.cargoshare.Preferiti.Preferiti
import com.example.cargoshare.Prenotazioni.Prenotazioni
import com.example.cargoshare.R
import com.example.cargoshare.Ricerca.CorsaAvviataFrag
import com.example.cargoshare.Ricerca.Mappe
import com.example.cargoshare.Ricerca.Ricerca
import com.example.cargoshare.databinding.ActivitySchermatahomeBinding
import com.google.android.gms.maps.model.LatLng


class SchermataHome : AppCompatActivity() {
    private var data1: String? = null
    private var orario: String? = null
    private var coordinate = arrayListOf<LatLng>()
    private var email: String? = null
    private lateinit var binding: ActivitySchermatahomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchermatahomeBinding.inflate(layoutInflater)



        val fragmentName = intent.getStringExtra("fragment")

        if (fragmentName != null) {
            Log.i("dai", "$fragmentName")
            when (fragmentName) {

                "sblocco" -> replaceBlocco()
            }

        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, Ricerca())
                .commit()
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {

                R.id.navigation_account -> replaceFragmentAccount(Account())
                R.id.navigation_favorites -> loadFragment(Preferiti())
                R.id.navigation_key -> replaceFragmentBlocco(Key())
                R.id.navigation_search -> loadFragment(Ricerca())
                R.id.navigation_prenotation -> loadFragment(Prenotazioni())

                else -> {
                }
            }
            true
        }


        setContentView(binding.root)


    }


    private fun loadFragment(fragment: Fragment) {



                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container, fragment)
                fragmentTransaction.commit()
            }



    private fun replaceFragmentAccount(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()



    }

    fun replaceBlocco() {

            val extras: Bundle? = intent.extras
            if (extras != null) {

                val targa = extras.getString("targa")
                val data = extras.getString("data")
                val orario = extras.getString("orario")
              //  val email = extras.getString("email")


                val bundle = Bundle().apply {
                    putString("email", email)
                    putString("data", data)
                    putString("orario", orario)
                    putString("targa", targa)
                }
                val intent=Intent(this, CorsaAvviataFrag::class.java)
                intent.putExtra("email", email)
                intent.putExtra("data", data)
                intent.putExtra("orario", orario)
                intent.putExtra("targa", targa)
                startActivity(intent)




        }
    }
    fun replaceFragmentBlocco(fragment: Fragment) {

        val extras: Bundle? = intent.extras
        if (extras != null) {

            val targa = extras.getString("targa")
            val data = extras.getString("data")
            val orario = extras.getString("orario")



            val bundle = Bundle().apply {
                putString("email", email)
                putString("data", data)
                putString("orario", orario)
                putString("targa", targa)
            }

             supportFragmentManager.setFragmentResult("fattura", bundle)


              val fragmentManager = supportFragmentManager
              val fragmentTransaction = fragmentManager.beginTransaction()
              fragmentTransaction.replace(R.id.fragment_container, fragment)
              fragmentTransaction.commit()



        }
    }
        fun lanciaActivity() {

            val intent = Intent(this, Profilo::class.java)
            startActivity(intent)

            }











        fun lanciaMappe(bundle: Bundle) {

            data1 = bundle.getString("data")
            orario = bundle.getString("orario")
            Log.i("dim", "sono in  $data1")
            coordinate = bundle.getParcelableArrayList<LatLng>("coordinate") as ArrayList<LatLng>

            val intent = Intent(this, Mappe::class.java).apply {
                putExtra("data", data1)
                putExtra("orario", orario)
                putExtra("coordinate", coordinate)

            }
            startActivity(intent)

        }
    fun lanciaSettings(){

        val intent= Intent(this, Settings::class.java)
        startActivity(intent)



    }





    }




