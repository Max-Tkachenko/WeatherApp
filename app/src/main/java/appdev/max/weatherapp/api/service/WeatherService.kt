package appdev.max.weatherapp.api.service

import appdev.max.weatherapp.model.response.DailyForecastResponse
import appdev.max.weatherapp.model.response.WeatherDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getWeatherDetail(
        @Query("appid") token: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "metric"
    ): Response<WeatherDetailResponse>

    @GET("onecall")
    suspend fun getWeatherForecast(
        @Query("appid") token: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "metric",
        @Query("exclude") exclude: String = "minutely,hourly"
    ): Response<DailyForecastResponse>

}