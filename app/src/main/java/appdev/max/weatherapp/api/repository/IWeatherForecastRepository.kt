package appdev.max.weatherapp.api.repository

import appdev.max.weatherapp.model.base.BaseErrorResponse
import appdev.max.weatherapp.model.base.Result
import appdev.max.weatherapp.model.response.DailyForecastResponse

interface IWeatherForecastRepository {

    suspend fun getWeatherForecast(latitude: Double, longitude: Double): Result<DailyForecastResponse, BaseErrorResponse>
}