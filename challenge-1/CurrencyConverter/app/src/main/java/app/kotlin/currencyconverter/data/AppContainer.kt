package app.kotlin.currencyconverter.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json

interface AppContainer {
    val ratesRepository: RatesRepository
    val userPreferenceRepository: UserPreferenceRepository
}

class DefaultAppContainer(dataStore: DataStore<Preferences>) : AppContainer {
    private val fetchRatesBaseUrl: String = "https://api.exchangeratesapi.io/v1"

    private val httpClient: HttpClient = HttpClient(Android) {
        install(plugin = ContentNegotiation) {
            json(
                contentType = ContentType(
                    contentType = "application",
                    contentSubtype = "json"
                )
            )
        }
    }

    override val ratesRepository: RatesRepository = NetworkRatesRepository(
        httpClient = httpClient,
        baseUrl = fetchRatesBaseUrl
    )

    override val userPreferenceRepository: UserPreferenceRepository =
        UserPreferenceRepository(dataStore = dataStore)
}