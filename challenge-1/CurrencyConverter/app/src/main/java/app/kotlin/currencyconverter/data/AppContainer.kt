package app.kotlin.currencyconverter.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json

/**
 * A container for application-level dependencies, providing instances of repositories.
 *
 * Implementations of this interface are responsible for managing and providing
 * instances of the repositories used throughout the application.
 */
interface AppContainer {

    /**
     * Provides access to the repository responsible for handling currency rate data.
     */
    val ratesRepository: RatesRepository

    /**
     * Provides access to the repository responsible for managing user preferences.
     */
    val userPreferenceRepository: UserPreferenceRepository
}

/**
 * The default implementation of [AppContainer] that sets up and provides application
 * dependencies such as repositories and HTTP clients.
 *
 * @param dataStore The [DataStore] instance used for managing user preferences.
 */
class DefaultAppContainer(dataStore: DataStore<Preferences>) : AppContainer {

    /** The base URL for fetching currency exchange rates data from the API. */
    private val fetchRatesBaseUrl: String = "https://api.exchangeratesapi.io/v1"

    /**
     * Configured [HttpClient] instance used to make network requests for currency rates.
     *
     * This client is configured with [ContentNegotiation] and JSON serialization for handling
     * JSON API responses.
     */
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

    /**
     * An implementation of [RatesRepository] that fetches currency rates from the network.
     */
    override val ratesRepository: RatesRepository = NetworkRatesRepository(
        httpClient = httpClient,
        baseUrl = fetchRatesBaseUrl
    )

    /**
     * An implementation of [UserPreferenceRepository] for managing and storing user preferences
     * using [DataStore].
     */
    override val userPreferenceRepository: UserPreferenceRepository =
        UserPreferenceRepository(dataStore = dataStore)
}
