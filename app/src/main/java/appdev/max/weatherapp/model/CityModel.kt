package appdev.max.weatherapp.model

import java.io.Serializable

data class CityModel(
    val id: Long,
    val name: String,
    val coordinates: List<Double>
) : Serializable