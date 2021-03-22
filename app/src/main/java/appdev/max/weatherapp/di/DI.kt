package appdev.max.weatherapp.di

import appdev.max.weatherapp.BuildConfig
import appdev.max.weatherapp.api.repository.IWeatherDetailRepository
import appdev.max.weatherapp.api.repository.IWeatherForecastRepository
import appdev.max.weatherapp.api.repository.WeatherRepository
import appdev.max.weatherapp.api.service.WeatherService
import appdev.max.weatherapp.arch.di.DICommon
import appdev.max.weatherapp.arch.di.IDIHolder
import appdev.max.weatherapp.managers.auth.AuthManager
import appdev.max.weatherapp.managers.auth.ITokenProvider
import appdev.max.weatherapp.screen.MainViewModel
import appdev.max.weatherapp.screen.detail.WeatherDetailViewModel
import appdev.max.weatherapp.screen.forecast.WeatherForecastViewModel
import com.google.gson.Gson
import okhttp3.Interceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.binds
import org.koin.dsl.module
import retrofit2.Retrofit

class DI : IDIHolder {

    override fun provideAppScopeModules(): List<Module> = listOf(
        _appModule,        // App
        _mainModule        // Main
    )

    override fun provideApiConfig(): DICommon.Api.Config? = DICommon.Api.Config(
        baseUrl = BuildConfig.API_URL,
        gson = Gson(),
        interceptors = listOf(
            Interceptor {
                it.proceed(
                    it.request().newBuilder()
                        .addHeader("User-Agent", System.getProperty("http.agent") ?: "")
                        .addHeader("Accept-Charset", "UTF-8")
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-type", "application/json")
                        .build()
                )
            }
        )
    )

    private val _appModule = module {
        single { AuthManager() } binds arrayOf(
            ITokenProvider::class
        )
    }

    private val _mainModule = module {
        factory { get<Retrofit>(named(DICommon.Api.API_MAIN)).create(WeatherService::class.java) }
        single { WeatherRepository(get(), get(), get()) } binds arrayOf(
            IWeatherDetailRepository::class,
            IWeatherForecastRepository::class
        )
        viewModel { MainViewModel() }
        viewModel { WeatherDetailViewModel(get()) }
        viewModel { WeatherForecastViewModel(get()) }
    }
}