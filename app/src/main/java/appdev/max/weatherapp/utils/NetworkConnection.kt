package appdev.max.weatherapp.utils

import  android.net.ConnectivityManager
import android.net.NetworkInfo

class NoInternetConnectionException : RuntimeException("No internet connection")

class NetworkConnection(private val connectivityManager: ConnectivityManager) {

    val connected: Boolean
        get() {
            var isConnected: Boolean? = false // Initial Value
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnected)
                isConnected = true
            return isConnected ?: false
        }
}

