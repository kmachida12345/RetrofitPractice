package com.example.retrofitpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.retrofitpractice.model.WeatherResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val api = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org")
                .build().create(ApiInterface::class.java)

        suspend fun getRequest(): WeatherResponse? {
            return try {
                val response: WeatherResponse = api.getWeather()
                response
            } catch (e: Exception) {
                null
            }
        }

        lifecycleScope.launch {
            val response = getRequest()
            Log.d("hoge response ", response.toString())
        }

    }

    companion object {
        private const val API_KEY = "ccc66e80fccf4425a48eba9617272b73"
        private const val TAG = "MainActivity"
        private const val API = "weather?lat={lat}&lon={lon}&appid={API key}"
    }
    interface ApiInterface {
        @GET("/data/2.5/weather?lat=35.425007&lon=139.6658144&appid=ccc66e80fccf4425a48eba9617272b73")
        suspend fun getWeather(): WeatherResponse
    }

}