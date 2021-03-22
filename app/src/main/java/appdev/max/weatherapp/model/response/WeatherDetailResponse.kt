package appdev.max.weatherapp.model.response

import appdev.max.weatherapp.model.TempModel
import appdev.max.weatherapp.model.WeatherModel
import java.io.Serializable

data class WeatherDetailResponse(
    val dt: Long,
    val main: TempModel,
    val wind: WindModel,
    val clouds: CloudsModel,
    val weather: List<WeatherModel>,
) : Serializable {

    data class WindModel(
        val speed: Double,
        val deg: Int
    ) : Serializable

    data class CloudsModel(
        val all: Int
    ) : Serializable
}

