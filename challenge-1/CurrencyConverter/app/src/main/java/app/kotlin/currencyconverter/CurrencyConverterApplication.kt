package app.kotlin.currencyconverter

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import app.kotlin.currencyconverter.data.AppContainer
import app.kotlin.currencyconverter.data.DefaultAppContainer

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCE_NAME)

class CurrencyConverterApplication : Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = DefaultAppContainer(dataStore = applicationContext.dataStore)
    }
}