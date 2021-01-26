package com.example.retrofitpractice.model

data class ForecastInfo(
    val list: List<WeatherInfo>
)

data class WeatherInfo(
    val dt: Int,
    val weather: List<Weather>,
    val name: String
) {
}

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
) {

}
