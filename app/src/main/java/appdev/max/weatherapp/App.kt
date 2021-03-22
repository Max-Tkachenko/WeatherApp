package appdev.max.weatherapp

import android.app.Application
import appdev.max.weatherapp.arch.di.DICommon
import appdev.max.weatherapp.di.DI

class App : Application() {

    companion object {
        private lateinit var instance: Application
        val context get() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // DI
        DICommon.init(this, DI())
    }
}