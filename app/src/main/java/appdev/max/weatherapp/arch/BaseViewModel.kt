package appdev.max.weatherapp.arch

import androidx.lifecycle.ViewModel
import appdev.max.weatherapp.utils.print
import kotlinx.coroutines.*

abstract class BaseViewModel : ViewModel() {

    /*_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_COROUTINES_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_*/

    private val job: Job = SupervisorJob()
    protected val coroutineScope: CoroutineScope =
        CoroutineScope(job + Dispatchers.Main + CoroutineExceptionHandler { _, throwable -> throwable.print() })
}