package appdev.max.weatherapp.utils

import android.annotation.SuppressLint
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.trello.rxlifecycle3.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

/*_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_VIEW_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_*/

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

@SuppressLint("CheckResult")
inline fun <T : View> T.onClick(throttle: Long = 400, crossinline action: (T) -> Unit) {
    if (throttle > 0) {
        RxView.clicks(this)
            .bindToLifecycle(this)
            .throttleFirst(throttle, TimeUnit.MILLISECONDS)
            .subscribe({ action(this) }, { it.print() })
    } else {
        this.setOnClickListener {
            action(this)
        }
    }
}