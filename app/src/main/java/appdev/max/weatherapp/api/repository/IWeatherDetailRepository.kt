package appdev.max.weatherapp.api.repository

import appdev.max.weatherapp.model.base.BaseErrorResponse
import appdev.max.weatherapp.model.base.Result
import appdev.max.weatherapp.model.response.WeatherDetailResponse

interface IWeatherDetailRepository {

    suspend fun getWeatherDetail(latitude: Double, longitude: Double): Result<WeatherDetailResponse, BaseErrorResponse>
}