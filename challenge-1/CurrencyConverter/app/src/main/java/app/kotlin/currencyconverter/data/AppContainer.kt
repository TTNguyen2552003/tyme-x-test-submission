package app.kotlin.currencyconverter.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json

interface AppContainer {
    val ratesRepository: RatesRepository
}

class DefaultAppContainer() : AppContainer {
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
}