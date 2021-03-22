package appdev.max.weatherapp.model.response

import appdev.max.weatherapp.model.TempModel
import appdev.max.weatherapp.model.WeatherModel
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DailyForecastResponse(
    val daily: List<DailyWeatherModel>
) : Serializable

data class DailyWeatherModel(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: TempModel,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    val weather: List<WeatherModel>,
    val clouds: Int
) : Serializable