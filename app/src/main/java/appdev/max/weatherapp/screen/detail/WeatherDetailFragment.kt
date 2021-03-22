package appdev.max.weatherapp.screen.detail

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import appdev.max.weatherapp.R
import appdev.max.weatherapp.arch.BaseFragment
import appdev.max.weatherapp.databinding.FragmentWeatherDetailBinding
import appdev.max.weatherapp.managers.location.LocationDelegate
import appdev.max.weatherapp.model.base.Result
import appdev.max.weatherapp.screen.MainActivity
import appdev.max.weatherapp.screen.MainViewModel
import appdev.max.weatherapp.utils.NoInternetConnectionException
import appdev.max.weatherapp.utils.onClick
import appdev.max.weatherapp.utils.toast
import appdev.max.weatherapp.utils.visible
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherDetailFragment : BaseFragment<MainActivity, FragmentWeatherDetailBinding>() {
    override val layoutRes: Int get() = R.layout.fragment_weather_detail

    // ViewModel
    private val _viewModel: WeatherDetailViewModel by viewModel()

    // Main ViewModel
    private val _mainViewModel: MainViewModel by sharedViewModel()

    // Location delegate
    private val _locationDelegate = LocationDelegate(this) { location ->
        // Update location when it's available
        _mainViewModel.run {
            locationLiveDta.value?.let { actualLocation ->
                if (actualLocation.first == location.latitude && actualLocation.second == location.longitude) {
                    return@run
                }
            }
            updateLocation(location.latitude, location.longitude)
        }
    }

    override fun onViewCreated() {
        // Binding setup
        binding.viewModel = _viewModel
        binding.mainViewModel = _mainViewModel

        // Choose city
        binding.weatherDetailBtnChooseCity.onClick {
            findNavController().navigate(R.id.action_weatherDetailFragment_to_citiesFragment)
        }

        // Use location
        binding.weatherDetailBtnUseLocation.onClick {

            initializeUserLocation()
            _mainViewModel.updateSelectedCity("")
        }

        // Try to get weather by user location on start
        if ((_mainViewModel.selectedCityLiveData.value ?: "").isEmpty()) {
            initializeUserLocation()
        }

        // Open daily weather forecast
        binding.weatherDetailTvForecast.onClick {
            findNavController().navigate(R.id.action_weatherDetailFragment_to_weatherForecastFragment)
        }

        // Get weather detail by location
        _mainViewModel.locationLiveDta.observe(viewLifecycleOwner, { (latitude, longitude) ->
            _viewModel.getWeatherDetail(latitude, longitude)
        })

        // Observe weather detail response
        _viewModel.weatherDetailResponseEventLiveData.observe(viewLifecycleOwner, { event ->
            event.consume()?.let { result ->
                when (result) {
                    is Result.Success -> {
                        // Show main content
                        binding.weatherDetailClMain.visible()
                    }
                    is Result.Error -> {
                        if (result.exception is NoInternetConnectionException) {
                            toast(R.string.common_error_internet_connection)
                        } else {
                            toast(result.errorMessage)
                        }
                    }
                }
            }
        })
    }

    private fun initializeUserLocation() {
        if (checkLocationPermissions()) {
            _locationDelegate.startLocationUpdates()
        }
    }

    private fun checkLocationPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LocationDelegate.LOCATION_ACCESS_GRANTED_REQUEST_CODE
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LocationDelegate.LOCATION_ACCESS_GRANTED_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            _locationDelegate.startLocationUpdates()
        } else {
            toast(R.string.allow_location_permission)
        }
    }
}