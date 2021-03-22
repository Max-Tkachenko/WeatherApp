package appdev.max.weatherapp.arch.di

import android.content.Context
import android.net.ConnectivityManager
import appdev.max.weatherapp.App
import appdev.max.weatherapp.utils.NetworkConnection
import com.google.gson.Gson
import appdev.max.weatherapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object DICommon {

    fun init(app: App, diHolder: IDIHolder) {
        startKoin {
            androidContext(app)

            val modules = mutableListOf(
                connectionModule
            ).apply {
                addAll(diHolder.provideAppScopeModules())
            }

            // Provide modules
            modules(modules)
        }

        // Provide api module api config != null
        diHolder.provideApiConfig()?.let { apiConfig ->
            loadKoinModules(provideApiModule(apiConfig))
        }
    }

    private val connectionModule = module {
        single { (get() as Context).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
        single { NetworkConnection(get()) }
    }

    /*_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_API_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_*/

    object Api {
        data class Config(
            val baseUrl: String,
            val interceptors: List<Interceptor> = listOf(),
            val gson: Gson = Gson(),
            val connectionTimeoutInSec: Long = 30,
            val readTimeoutInSec: Long = 60,
            val writeTimeoutInSec: Long = 60
        )

        // Api
        const val BASE_URL = "base_url"
        const val API_MAIN = "api_main"
        const val API_CLIENT = "api_client"

        // Interceptors
        const val LOGGING_INTERCEPTOR = "logging_interceptor"
    }

    private fun provideApiModule(apiConfig: Api.Config) = module {
        // Base url
        single(named(Api.BASE_URL)) { apiConfig.baseUrl }
        // Gson
        single { apiConfig.gson }
        // Gson converter
        single { GsonConverterFactory.create(apiConfig.gson) }
        // Logging interceptor
        single<Interceptor>(named(Api.LOGGING_INTERCEPTOR)) {
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }
        }
        // OkHttpClient
        factory(named(Api.API_CLIENT)) {
            OkHttpClient.Builder().apply {
                // Add Api.Config interceptors
                apiConfig.interceptors.forEach { interceptor -> addInterceptor(interceptor) }
                addInterceptor(get<Interceptor>(named(Api.LOGGING_INTERCEPTOR)))
                connectTimeout(apiConfig.connectionTimeoutInSec, TimeUnit.SECONDS)
                readTimeout(apiConfig.readTimeoutInSec, TimeUnit.SECONDS)
            }.build()
        }
        // Retrofit MAIN
        single(named(Api.API_MAIN)) {
            Retrofit.Builder()
                .baseUrl(get<String>(named(Api.BASE_URL)))
                .addConverterFactory(get<GsonConverterFactory>())
                .client(get(named(Api.API_CLIENT)))
                .build() as Retrofit
        }
    }

}