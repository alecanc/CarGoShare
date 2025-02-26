package com.example.cargoshare.Ricerca


import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.Profilo.SchermataHome
import com.example.cargoshare.R
import com.example.cargoshare.databinding.MappeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.*
import com.google.gson.JsonArray
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Mappe: AppCompatActivity(), OnMapReadyCallback  {

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: MapView
    private lateinit var binding: MappeBinding
    private lateinit var googleMap: GoogleMap

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            //TODO ritornare i dati corretti
            val latitudine = data?.getDoubleExtra("latitudine", 0.0)
            val longitudine = data?.getDoubleExtra("longitudine",0.0)
            if (longitudine != null) {
                if (latitudine != null) {
                    findCar(latitudine,longitudine)
                }
            }

        }
    }

    val posizioni = arrayListOf<LatLng>()
    var data:String?=null
    var orario:String?=null
    private var posizione:LatLng?=null

    companion object {
        private const val LOCATION_REQUEST_CODE = 1

    }









    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MappeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val extras: Bundle? = intent.extras
        if (extras != null) {

            val coordinate = extras.getParcelableArrayList<LatLng>("coordinate")
            if (coordinate != null) {
                Log.i("dim", "le coordinate ${coordinate.size}")
                for (i in 0 until coordinate.size)
                    posizioni.add(coordinate[i])


            }
            data= extras.getString("data").toString()
            orario= extras.getString("orario").toString()

            Log.i("da","sono i ${data}" )





        }



        mMap = binding.map1
        mMap.onCreate(savedInstanceState)
        mMap.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding.backMap.setOnClickListener {
            finish()
        }
binding.listaBtn.setOnClickListener {
    val intent=Intent(this@Mappe, ListaCar::class.java)
    intent.putExtra("data", data)
    intent.putExtra("orario", orario)

    startForResult.launch(intent)
}


    }







    override fun onMapReady(map: GoogleMap) {

        googleMap = map
        checkLocationPermission()
        try {
            val success =
                googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        this,
                      R.raw.map_style
                    )
                )
            if (!success) {
                Log.e("MapsActivity", "Style parsing failed")
            }
        } catch (e: Resources.NotFoundException) {

            Log.e("MapsActivity", "Map style resource not found.")
        }
        googleMap.uiSettings.isZoomControlsEnabled = true

        //  googleMap.setOnMyLocationButtonClickListener()
        setUpMap()
      googleMap.setOnMarkerClickListener { marker ->

            onMarkerCl(marker)
            marker.showInfoWindow()

            false
        }



        googleMap.uiSettings.isMapToolbarEnabled = true

    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE)
        } else {
            setUpMap()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpMap()
            } else {
               val intent= Intent(this, SchermataHome::class.java )
                startActivity(intent)
            }
        }
    }
    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")

        googleMap.addMarker(markerOptions)
    }
    fun creaMarkerOptionsPerPosizioni(posizioni: List<LatLng>): List<MarkerOptions> {
        val markerOptionsList = mutableListOf<MarkerOptions>()

        for (posizione in posizioni) {
            val markerOptions = creaMarkerOptions(posizione)
            markerOptionsList.add(markerOptions)
        }

        return markerOptionsList
    }

    fun creaMarkerOptions(posizione: LatLng): MarkerOptions {
        return MarkerOptions()
            .position(posizione)
           .icon(BitmapDescriptorFactory.fromResource(com.example.cargoshare.R.drawable.location_2))

    }
    private fun setUpMap() {

        if (ActivityCompat.checkSelfPermission(
                this,ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return
        }


        googleMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                val markerOptionsList = creaMarkerOptionsPerPosizioni(posizioni)


                for (markerOptions in markerOptionsList) {
                    googleMap.addMarker(markerOptions)
                }
                placeMarkerOnMap(currentLatLong)

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))


            }

        }
    }





    override fun onResume() {
        super.onResume()
        mMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMap.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMap.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMap.onLowMemory()
    }





    fun onMarkerCl(p0: Marker): Boolean {
        binding.btnMarker.visibility = View.VISIBLE


             val latitudine= p0.position.latitude
           val longitudine=p0.position.longitude



         binding.btnMarker.setOnClickListener {
binding.btnMarker.visibility = View.GONE

               val query =
                   "select targa, foto, num_posti, indirizzo, citta, colore, tipologia, marca, modello, cambio,tariffa, anno, email_p, nome_p, cognome_p, num_telefono_p, num_stars,foto_p,  descrizione from Macchina Join Proprietario Where latitudine= '${latitudine}' AND longitudine= '${longitudine}' AND email_p=id_proprietario; "
               ClientNetwork.retrofit.ricerca(query).enqueue(
                   object : Callback<JsonObject> {
                       override fun onResponse(
                           call: Call<JsonObject>,
                           response: Response<JsonObject>
                       ) {
                           if (response.isSuccessful) {
                               if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                                   val queryset = response.body()?.get("queryset") as JsonArray


                                    val targa = (queryset.get(0) as JsonObject).get("targa").asString
                                   val num_posti =
                                       (queryset.get(0) as JsonObject).get("num_posti").asInt
                                   val indirizzo =
                                       (queryset.get(0) as JsonObject).get("indirizzo").asString
                                   val citta = (queryset.get(0) as JsonObject).get("citta").asString
                                   val colore = (queryset.get(0) as JsonObject).get("colore").asString
                                   val tipologia =
                                       (queryset.get(0) as JsonObject).get("tipologia").asString
                                   val marca = (queryset.get(0) as JsonObject).get("marca").asString
                                   val modello = (queryset.get(0) as JsonObject).get("modello").asString
                                   val cambio = (queryset.get(0) as JsonObject).get("cambio").asString
                                   val tariffa = (queryset.get(0) as JsonObject).get("tariffa").asFloat
                                   val anno = (queryset.get(0) as JsonObject).get("anno").asString
                                    val foto= (queryset.get(0) as JsonObject).get("foto").asString
                                   val foto_p= (queryset.get(0) as JsonObject).get("foto_p").asString
                                   val email_p =
                                       (queryset.get(0) as JsonObject).get("email_p").asString
                                   val nome_p = (queryset.get(0) as JsonObject).get("nome_p").asString
                                   val cognome_p =
                                       (queryset.get(0) as JsonObject).get("cognome_p").asString
                                   val numero_tel =
                                       (queryset.get(0) as JsonObject).get("num_telefono_p").asInt
                                   val stelle =
                                       (queryset.get(0) as JsonObject).get("num_telefono_p").asInt
                                   val descrizione =
                                       (queryset.get(0) as JsonObject).get("descrizione").asString


                                   val bundle = Bundle().apply {
                                       putString("targa", targa)
                                       putInt("num_posti", num_posti)
                                       putString("indirizzo", indirizzo)
                                       putString("citta", citta)
                                       putString("colore", colore)
                                       putString("tipologia", tipologia)
                                       putString("marca", marca)
                                       putString("modello", modello)
                                       putString("cambio", cambio)
                                       putFloat("tariffa", tariffa)
                                       putString("anno", anno)
                                       putString("email_p", email_p)
                                       putString("nome_p", nome_p)
                                       putString("cognome_p", cognome_p)
                                       putInt("numero_tel", numero_tel)
                                       putInt("stelle", stelle)
                                       putString("descrizione", descrizione)
                                       putString("data", data)
                                       putString("foto", foto)
                                       putString("foto_p", foto_p)
                                       putString("orario", orario)



                                   }
                                   supportFragmentManager.setFragmentResult("carInfo", bundle)



                                   val carFragment = CarFragment()
                                   supportFragmentManager.beginTransaction()

                                       .add(R.id.fragmentContainerView2, carFragment)
                                       .addToBackStack(null)
                                       .commit()


                               } else {
                                   Toast.makeText(
                                       this@Mappe,
                                       "errore",
                                       Toast.LENGTH_LONG
                                   ).show()

                               }
                           } else {
                               Toast.makeText(
                                   this@Mappe,
                                   "Errore nella risposta:",
                                   Toast.LENGTH_LONG
                               ).show()
                           }


                       }

                       override fun onFailure(call: Call<JsonObject>, t: Throwable) {


                       }

                   }
               )
           }
        return true }


fun findCar(latitudine:Double, longitudine:Double ){
    val posizione= LatLng(latitudine, longitudine)
    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posizione, 12f))

}




}










