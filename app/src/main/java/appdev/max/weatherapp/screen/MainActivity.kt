package appdev.max.weatherapp.screen

import android.os.Bundle
import appdev.max.weatherapp.R
import appdev.max.weatherapp.arch.BaseActivity
import appdev.max.weatherapp.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutRes: Int get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}