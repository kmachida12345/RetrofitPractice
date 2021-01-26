package com.example.retrofitpractice

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.retrofitpractice.model.ForecastInfo
import com.example.retrofitpractice.model.WeatherInfo
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val api = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org")
                .build().create(ApiInterface::class.java)

        // 公式より https://developer.android.com/training/permissions/requesting?hl=ja#allow-system-manage-request-code
        // Register the permissions callback, which handles the user's response to the
        // system permissions dialog. Save the return value, an instance of
        // ActivityResultLauncher. You can use either a val, as shown in this snippet,
        // or a lateinit var in your onAttach() or onCreate() method.
        val requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your
                        // app.
                        Log.d(TAG, "onCreate: hoge permission granted")

                        fusedLocationClient.lastLocation
                                .addOnSuccessListener { location: Location? ->
                                    // Got last known location. In some rare situations this can be null.
                                    Log.d(TAG, "onCreate: hoge location=$location")

                                    if (location == null)
                                        return@addOnSuccessListener

                                    lifecycleScope.launch {
                                        val response = api.getWeather(location.latitude, location.longitude, API_KEY)
                                        Log.d("hoge response ", response.toString())
                                        val forecastRes = api.getWeatherForecast(location.latitude, location.longitude, API_KEY)
                                        Log.d("hoge forecastRes ", forecastRes.toString())
                                    }
                                }

                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                        Log.d(TAG, "onCreate: hoge permission denied")
                    }
                }

        when {
            ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.

                fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            // Got last known location. In some rare situations this can be null.
                            Log.d(TAG, "onCreate: hoge location=$location")

                            if (location == null)
                                return@addOnSuccessListener

                            lifecycleScope.launch {
                                val response = api.getWeather(location.latitude, location.longitude, API_KEY)
                                Log.d("hoge response ", response.toString())
                                val forecastRes = api.getWeatherForecast(location.latitude, location.longitude, API_KEY)
                                Log.d("hoge forecastRes ", forecastRes.toString())

                                val imageUrl = "http://openweathermap.org/img/wn/${response.weather[0].icon}@2x.png"

                                Log.d(TAG, "onCreate: hoge imageUrl=$imageUrl")

                                launch(Dispatchers.Main) {
                                    Glide.with(applicationContext).load("https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png").into(findViewById(R.id.hoge_image))
                                }

                            }
                        }
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
//            showInContextUI(...)
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                        Manifest.permission.ACCESS_COARSE_LOCATION)
            }
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