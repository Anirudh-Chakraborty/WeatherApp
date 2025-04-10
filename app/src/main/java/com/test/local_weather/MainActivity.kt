package com.test.local_weather

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.text.set
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.color.utilities.ViewingConditions
import com.test.local_weather.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale
import java.util.concurrent.locks.Condition


//Api Key-1636d8d1b515d3d6cd61313cd4f773f4
class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        fetchWeatherData("London")
        setupSearchCity()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupSearchCity() {
        val searchView = binding.dhundLo
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }


    private fun fetchWeatherData(cityname:String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response = retrofit.getWeatherData(cityname,"1636d8d1b515d3d6cd61313cd4f773f4", "metric")
        response.enqueue(object : Callback<Local_Weather>{
            override fun onResponse(call: Call<Local_Weather>, response: Response<Local_Weather>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody!=null){
                    val temp =responseBody.main.temp.toInt()
                    val humi =responseBody.main.humidity.toInt()
                    val winds =responseBody.wind.speed.toInt()
                    val pressure = responseBody.main.pressure.toInt()
                    val name = responseBody.name.toString()
                    val country = responseBody.sys.country.toString()
                    val condition = responseBody.weather.firstOrNull()?.main?:"unknow"
                    val millis = System.currentTimeMillis();
                    val sun = responseBody.sys.sunrise.toLong()
                    val timeZone = responseBody.timezone






                    binding.weather.text="$condition"
                    binding.humidity.text="$humi %"
                    binding.cityname.text="$name"
                    binding.temperature.text="$temp"
                    binding.pressure.text="$pressure mbar"
                    binding.windspeed.text="$winds km/h"
                    binding.tarikh.text = dateConverter(millis)
                   // binding.samay.text = "${timeConverter(sun)}"


                    //val weat = responseBody.main.
                    // Log.d(TAG, "rainfall: $rainfall")
                   // Log.d(TAG, "Temperature: $temp")
                    //Log.d(TAG, "Pressure: $pressure")


                    changeImageAccordindgToMausam(condition)

                }
            }

            override fun onFailure(call: Call<Local_Weather>, t: Throwable) {

            }

        })

        }

    private fun changeImageAccordindgToMausam(conditions: String) {
        when (conditions){
            "Haze","Mist"->{
                binding.root.setBackgroundResource(R.drawable.mist_1)
                binding.weatherImage.setImageResource(R.drawable.mist_2)
            }
            "Clear","Sunny"->{
                binding.root.setBackgroundResource(R.drawable.clear_morning)
                binding.weatherImage.setImageResource(R.drawable.sun_1)
            }
            "Rain"->{
                binding.root.setBackgroundResource(R.drawable.cloudy_night)
                binding.weatherImage.setImageResource(R.drawable.rai_f_ll)
            }
            "Clouds",->{
                binding.root.setBackgroundResource(R.drawable.cloudy_night)
                binding.weatherImage.setImageResource(R.drawable.cloudy_sun)
            }
            "Thunderstorm",->{
                binding.root.setBackgroundResource(R.drawable.cloudy_night)
                binding.weatherImage.setImageResource(R.drawable.thunder_storm)
            }
            else ->{
                binding.root.setBackgroundResource(R.drawable.clear_morning)
                binding.weatherImage.setImageResource(R.drawable.sun_1)
            }
        }
    }

    private fun dateConverter(timestamp: Long): String? {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

//    private fun timeConverter(timeZone: Int): String {
//        val sdf = SimpleDateFormat("HH:mm:ss a", Locale.getDefault())
//       // return sdf.format(Date(timestamp*1000))
//
//    }


    }

