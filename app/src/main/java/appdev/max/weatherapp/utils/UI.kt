package appdev.max.weatherapp.utils

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import appdev.max.weatherapp.arch.BaseFragment

/*_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_FRAGMENT_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_*/

fun Fragment.toast(@StringRes res: Int) {
    Toast.makeText(requireContext(), res, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

@Suppress("LeakingThis")
abstract class FragmentViewLifecycleObserver(baseFragment: BaseFragment<*, *>) : LifecycleObserver {

    init {
        baseFragment.viewLifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun afterCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun afterViewDestroy() {
    }
}