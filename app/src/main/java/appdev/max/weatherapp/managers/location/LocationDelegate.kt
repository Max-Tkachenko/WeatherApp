package appdev.max.weatherapp.managers.location

import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import appdev.max.weatherapp.arch.BaseFragment
import appdev.max.weatherapp.utils.FragmentViewLifecycleObserver
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LocationDelegate(
    private val fragment: BaseFragment<*, *>,
    private val onLocationAvailable: (LatLng) -> Unit
) : FragmentViewLifecycleObserver(fragment) {

    companion object {
        const val LOCATION_ACCESS_GRANTED_REQUEST_CODE = 4040
    }

    // Location LiveData
    private val _locationLiveData = MutableLiveData<LatLng>()

    // Handle fragment lifecycle events
    override fun afterCreate() {
        super.afterCreate()
        initLocationCallbackAndRequest()
    }

    override fun afterViewDestroy() {
        super.afterViewDestroy()
        stopLocationUpdates()
    }

    // Location
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    private fun initLocationCallbackAndRequest() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(fragment.requireContext())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    val actualLocation = LatLng(location.latitude, location.longitude)
                    onLocationAvailable(actualLocation)
                    _locationLiveData.postValue(actualLocation)
                }
            }
        }
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
    }

    fun startLocationUpdates() {
        _locationLiveData.value?.let { actualLocation ->
            onLocationAvailable(actualLocation)
            return
        }
        GlobalScope.launch(Dispatchers.Main) {
            fragment.run {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    fusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                }
            }
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }
}