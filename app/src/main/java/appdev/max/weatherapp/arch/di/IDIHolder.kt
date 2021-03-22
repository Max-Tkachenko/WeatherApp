package appdev.max.weatherapp.arch.di

import org.koin.core.KoinComponent
import org.koin.core.module.Module

interface IDIHolder : KoinComponent {

    fun provideAppScopeModules(): List<Module>

    fun provideApiConfig(): DICommon.Api.Config?
}