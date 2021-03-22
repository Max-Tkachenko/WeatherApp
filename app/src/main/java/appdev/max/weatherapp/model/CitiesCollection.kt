package appdev.max.weatherapp.model

import java.io.Serializable

data class CitiesCollection(
    val cities: List<CityModel>
) : Serializable