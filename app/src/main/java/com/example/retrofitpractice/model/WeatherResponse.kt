package com.example.retrofitpractice.model

data class WeatherResponse(
    val weather: List<Weather>,
) {
}

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
) {

}
