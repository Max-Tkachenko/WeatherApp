package appdev.max.weatherapp.screen.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import appdev.max.weatherapp.api.repository.IWeatherForecastRepository
import appdev.max.weatherapp.arch.BaseViewModel
import appdev.max.weatherapp.model.base.BaseErrorResponse
import appdev.max.weatherapp.model.base.Event
import appdev.max.weatherapp.model.base.Result
import appdev.max.weatherapp.model.response.DailyForecastResponse
import appdev.max.weatherapp.utils.plusAssign
import kotlinx.coroutines.launch

class WeatherForecastViewModel(private val _weatherForecastRepository: IWeatherForecastRepository) : BaseViewModel() {

    // Weather forecast LiveData
    private val _weatherForecastResponseLiveData: MutableLiveData<Result<DailyForecastResponse, BaseErrorResponse>> = MutableLiveData()
    val weatherForecastResponseEventLiveData: LiveData<Event<Result<DailyForecastResponse, BaseErrorResponse>>> =
        _weatherForecastResponseLiveData.map { Event(it) }

    // UI
    val isProgress = MutableLiveData(false)

    fun getWeatherForecast(latitude: Double, longitude: Double) {
        coroutineScope.launch {
            isProgress.postValue(true)
            _weatherForecastResponseLiveData += _weatherForecastRepository.getWeatherForecast(latitude, longitude)
            isProgress.postValue(false)
        }
    }
}