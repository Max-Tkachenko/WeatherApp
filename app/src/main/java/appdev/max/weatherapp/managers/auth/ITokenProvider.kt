package appdev.max.weatherapp.managers.auth

interface ITokenProvider {
    fun provideToken(): String
}