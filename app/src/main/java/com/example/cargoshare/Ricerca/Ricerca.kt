package com.example.cargoshare.Ricerca

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ProgressBar
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.cargoshare.ClientWeb.ClientNetwork
import com.example.cargoshare.Profilo.SchermataHome
import com.example.cargoshare.R
import com.example.cargoshare.databinding.FragmentSearchBinding
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*



class Ricerca : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var datePickerDialog: DatePickerDialog
    private var hour = 0
    private var minute = 0
    private var data:String?=null
    private var orario:String?=null


    private fun getTodaysDate(): String {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        var month = cal.get(Calendar.MONTH)
        month = month + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        return makeDateString(day, month, year)
    }

    private fun initDatePicker() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                var month = month
                month = month + 1
                val date = makeDateString(day, month, year)
                binding.datePickerButton.text = date

            }

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)

        val day = cal.get(Calendar.DAY_OF_MONTH)

        val style = AlertDialog.THEME_HOLO_LIGHT

        datePickerDialog =
            DatePickerDialog(requireContext(), style, dateSetListener, year, month, day)



    }




    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return "" + day + " " + getMonthFormat(month) + " " + year
    }

    private fun getMonthFormat(month: Int): String {
        return when (month) {
            1 -> "JAN"
            2 -> "FEB"
            3 -> "MAR"
            4 -> "APR"
            5 -> "MAY"
            6 -> "JUN"
            7 -> "JUL"
            8 -> "AUG"
            9 -> "SEP"
            10 -> "OCT"
            11 -> "NOV"
            12 -> "DEC"
            else -> "JAN"
        }
    }

    fun openDatePicker(view: View) {
        datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()
    }

    fun popTimePicker(view: View) {
        val onTimeSetListener =
            TimePickerDialog.OnTimeSetListener { timePicker: TimePicker, selectedHour: Int, selectedMinute: Int ->
                hour = selectedHour
                minute = selectedMinute
                binding.timeButton.text =
                    String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
            }



        val timePickerDialog =
            TimePickerDialog(requireContext(), onTimeSetListener, hour, minute, true)

        timePickerDialog.setTitle("Seleziona l'Orario")
        timePickerDialog.show()
    }

    fun onFailure(call: Call<JsonObject>, t: Throwable) {
        TODO("Not yet implemented")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)

        initDatePicker()

        binding.datePickerButton.text = getTodaysDate()
        val currentTime = getCurrentTime()
        val currentHour = currentTime.first
        val currentMinute = currentTime.second
        binding.timeButton.text = String.format(Locale.getDefault(), "%02d:%02d", currentHour, currentMinute)


        binding.datePickerButton.setOnClickListener {
            openDatePicker(binding.datePickerButton)
        }
        binding.timeButton.setOnClickListener {
            popTimePicker(binding.timeButton)
        }

        binding.conv.setOnClickListener {
             data = binding.datePickerButton.text.toString()
orario=binding.timeButton.text.toString()

            getMacchineLibere()


        }

        return binding.root
    }
    private fun getCurrentTime(): Pair<Int, Int> {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        return Pair(currentHour, currentMinute)
    }


    private fun getMacchineLibere() {

        val customFormatter = DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("d MMM yyyy")
            .toFormatter(Locale.ENGLISH)

        val dataObj = LocalDate.parse(data, customFormatter)
        val dataFormatted = dataObj.format(DateTimeFormatter.ISO_DATE)

       val ora = LocalTime.parse(orario)

        val oraFormatted = ora.format(DateTimeFormatter.ofPattern("HH:mm:ss"))


        val coordinate = arrayListOf<LatLng>()
        val query =
            "(select latitudine, longitudine from Macchina  Where prenotazione = 0) UNION\n" +
                    "         (select latitudine, longitudine from Macchina JOIN Prenotazione Where prenotazione = 0 AND id_macchina=Targa AND Data='$dataFormatted' AND Orario='$oraFormatted');"
        ClientNetwork.retrofit.ricerca(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        if ((response.body()?.get("queryset") as JsonArray).size() >= 1) {
                            val queryset = response.body()?.get("queryset") as JsonArray
                            for (i in 0 until queryset.size()) {


                                coordinate.add(
                                    LatLng(
                                        (queryset.get(i) as JsonObject).get("latitudine").asDouble,
                                        (queryset.get(i) as JsonObject).get("longitudine").asDouble
                                    )
                                )



                            }
                            val dialog = Dialog(requireContext())
                            dialog.setContentView(R.layout.caricamento)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


                            val duration = 3000L // 3 secondi
                            val progressBar = dialog.findViewById<ProgressBar>(R.id.progressBar)
                            progressBar.indeterminateDrawable =
                                ContextCompat.getDrawable(requireContext(), R.drawable.logo)
                            dialog.show()

                            Handler(Looper.getMainLooper()).postDelayed({
                                dialog.dismiss()
                            }, duration)



                            val result = Bundle().apply {
                                putString("data", "$dataFormatted")
                                putString("orario", "$oraFormatted")
                                putParcelableArrayList("coordinate", coordinate)

                            }


                            setFragmentResult("requestKey", bundleOf("bundleKey" to result))


                            (requireActivity() as SchermataHome).lanciaMappe(result)



                        } else {
                            Toast.makeText(
                                requireContext(),
                                "errore",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
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


}