package appdev.max.weatherapp.api.repository

import appdev.max.weatherapp.api.service.WeatherService
import appdev.max.weatherapp.arch.BaseRepository
import appdev.max.weatherapp.managers.auth.ITokenProvider
import appdev.max.weatherapp.model.base.BaseErrorResponse
import appdev.max.weatherapp.model.base.Result
import appdev.max.weatherapp.model.response.DailyForecastResponse
import appdev.max.weatherapp.model.response.WeatherDetailResponse
import appdev.max.weatherapp.utils.NetworkConnection

class WeatherRepository(
    networkConnection: NetworkConnection,
    private val _weatherService: WeatherService,
    private val _tokenProvider: ITokenProvider
) : BaseRepository(networkConnection),
    IWeatherDetailRepository,
    IWeatherForecastRepository {

    /*_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_IWeatherDetailRepository_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_*/

    override suspend fun getWeatherDetail(latitude: Double, longitude: Double): Result<WeatherDetailResponse, BaseErrorResponse> {
        val result: Result<Any, BaseErrorResponse> = processResponse(
            suspend { _weatherService.getWeatherDetail(_tokenProvider.provideToken(), latitude, longitude) })

        return when (result) {
            is Result.Success -> {
                return Result.Success(result.data as WeatherDetailResponse)
            }
            is Result.Error -> result
        }
    }

    /*_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_IWeatherForecastRepository_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_*/

    override suspend fun getWeatherForecast(latitude: Double, longitude: Double): Result<DailyForecastResponse, BaseErrorResponse> {
        val result: Result<Any, BaseErrorResponse> = processResponse(
            suspend { _weatherService.getWeatherForecast(_tokenProvider.provideToken(), latitude, longitude) })

        return when (result) {
            is Result.Success -> {
                return Result.Success(result.data as DailyForecastResponse)
            }
            is Result.Error -> result
        }
    }
}