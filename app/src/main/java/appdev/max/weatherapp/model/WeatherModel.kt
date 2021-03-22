package appdev.max.weatherapp.model

import java.io.Serializable

data class WeatherModel(
    val main: String,
    val description: String
) : Serializable