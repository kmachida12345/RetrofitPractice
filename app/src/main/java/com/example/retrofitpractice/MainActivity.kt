package com.example.retrofitpractice

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.retrofitpractice.model.ForecastInfo
import com.example.retrofitpractice.model.WeatherInfo
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val api = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org")
                .build().create(ApiInterface::class.java)

        lifecycleScope.launch {
            val response = api.getWeather(35.425007, 139.6658144, API_KEY)
            Log.d("hoge response ", response.toString())
            val forecastRes = api.getWeatherForecast(35.425007, 139.6658144, API_KEY)
            Log.d("hoge forecastRes ", forecastRes.toString())
        }
    }

    companion object {
        private const val API_KEY = "ccc66e80fccf4425a48eba9617272b73"
        private const val TAG = "MainActivity"
    }
    interface ApiInterface {
        @GET("/data/2.5/weather/")
        suspend fun getWeather(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") appId: String): WeatherInfo

        @GET("/data/2.5/forecast/")
        suspend fun getWeatherForecast(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") appId: String): ForecastInfo
    }

}