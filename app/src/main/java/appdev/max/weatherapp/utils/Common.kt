package appdev.max.weatherapp.utils

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import appdev.max.weatherapp.App

inline fun errorSafety(onError: (e: Exception) -> Unit = { it.print() }, action: () -> Unit) {
    try {
        action()
    } catch (e: Exception) {
        onError(e)
    }
}

fun Throwable.print() {
    errorSafety(onError = {}) {
        Log.e("Exception(C)", (this as? Exception)?.toString() ?: this.message.toString())
        printStackTrace()
    }
}

/*_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_TEXT_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_*/

fun getString(@StringRes string: Int): String = App.context.getString(string)

/*_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_LiveData_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_*/

operator fun <T> MutableLiveData<T>.plusAssign(value: T) = postValue(value)