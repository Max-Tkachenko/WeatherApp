package appdev.max.weatherapp.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import appdev.max.weatherapp.App
import appdev.max.weatherapp.arch.BaseViewModel
import appdev.max.weatherapp.model.CitiesCollection
import appdev.max.weatherapp.model.CityModel
import com.google.gson.Gson

class MainViewModel : BaseViewModel() {

    // Location LiveData
    private val _locationLiveData: MutableLiveData<Pair<Double, Double>> = MutableLiveData()
    val locationLiveDta: LiveData<Pair<Double, Double>> = _locationLiveData

    // Selected city LiveData
    private val _selectedCityLiveData: MutableLiveData<String> = MutableLiveData()
    val selectedCityLiveData: LiveData<String> = _selectedCityLiveData

    // Cities LiveData
    private val _citiesLiveData: MutableLiveData<List<CityModel>> = MutableLiveData()
    val citiesLiveDta: LiveData<List<CityModel>> = _citiesLiveData

    init {
        // Parse cities json file
        App.context.assets.open("cities.json").bufferedReader().use { it.readText() }.let { json ->
            val citiesCollection: CitiesCollection = Gson().fromJson(json, CitiesCollection::class.java)

            // Save parsed cities to LiveData
            _citiesLiveData.postValue(citiesCollection.cities)
        }
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        _locationLiveData.postValue(Pair(latitude, longitude))
    }

    fun updateSelectedCity(city: String) {
        _selectedCityLiveData.postValue(city)
    }
}