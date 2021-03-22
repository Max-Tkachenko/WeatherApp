package appdev.max.weatherapp.managers.auth

import appdev.max.weatherapp.BuildConfig

class AuthManager : ITokenProvider {

    /*_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ITokenProvider_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_*/

    override fun provideToken() = BuildConfig.API_KEY
}