package app.kotlin.currencyconverter

import android.app.Application
import app.kotlin.currencyconverter.data.AppContainer
import app.kotlin.currencyconverter.data.DefaultAppContainer

class CurrencyConverterApplication : Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = DefaultAppContainer()
    }
}