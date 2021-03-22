package appdev.max.weatherapp.screen.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import appdev.max.weatherapp.R
import appdev.max.weatherapp.api.repository.IWeatherDetailRepository
import appdev.max.weatherapp.arch.BaseViewModel
import appdev.max.weatherapp.model.base.BaseErrorResponse
import appdev.max.weatherapp.model.base.Event
import appdev.max.weatherapp.model.base.Result
import appdev.max.weatherapp.model.response.WeatherDetailResponse
import appdev.max.weatherapp.utils.errorSafety
import appdev.max.weatherapp.utils.forecastDateFormatted
import appdev.max.weatherapp.utils.getString
import appdev.max.weatherapp.utils.plusAssign
import kotlinx.coroutines.launch

class WeatherDetailViewModel(private val _weatherDetailRepository: IWeatherDetailRepository) : BaseViewModel() {

    // Weather detail LiveData
    private val _weatherDetailResponseLiveData: MutableLiveData<Result<WeatherDetailResponse, BaseErrorResponse>> = MutableLiveData()
    val weatherDetailResponseEventLiveData: LiveData<Event<Result<WeatherDetailResponse, BaseErrorResponse>>> = _weatherDetailResponseLiveData.map { Event(it) }

    // Weather detail fields
    val date = MutableLiveData("")
    val temp = MutableLiveData("")
    val pressure = MutableLiveData("")
    val humidity = MutableLiveData("")
    val clouds = MutableLiveData("")
    val windSpeed = MutableLiveData("")
    val windDegree = MutableLiveData("")
    val weatherDescription = MutableLiveData("")

    // UI
    val isProgress = MutableLiveData(false)

    fun getWeatherDetail(latitude: Double, longitude: Double) {
        coroutineScope.launch {
            isProgress.postValue(true)
            _weatherDetailResponseLiveData += _weatherDetailRepository.getWeatherDetail(latitude, longitude).also { result ->
                if (result is Result.Success) {
                    result.data?.let { weatherResponse ->
                        // Fill LiveData fields
                        date.postValue(forecastDateFormatted(weatherResponse.dt))
                        temp.postValue(String.format("%.1f", weatherResponse.main.temp).plus("°С"))
                        pressure.postValue(String.format(getString(R.string.pressure), weatherResponse.main.pressure))
                        humidity.postValue(String.format(getString(R.string.humidity), weatherResponse.main.humidity))
                        clouds.postValue(String.format(getString(R.string.clouds), weatherResponse.clouds.all).plus("%"))
                        windSpeed.postValue(String.format(getString(R.string.wind_speed), weatherResponse.wind.speed.toInt()))
                        windDegree.postValue(String.format(getString(R.string.wind_degree), weatherResponse.wind.deg))
                        errorSafety {
                            weatherDescription.postValue(weatherResponse.weather[0].description)
                        }
                    }
                }
            }
            isProgress.postValue(false)
        }
    }
}