package app.kotlin.currencyconverter.data

import app.kotlin.currencyconverter.data.models.CurrencyRatesResponse
import app.kotlin.currencyconverter.network.fetchRates
import io.ktor.client.HttpClient

/**
 * A network-based implementation of [RatesRepository] that fetches currency rate data
 * from a remote API using the provided [HttpClient] and base URL.
 *
 * This class is responsible for constructing the network request based on specified endpoints
 * and query parameters, and then returning the API response as a [CurrencyRatesResponse].
 *
 * @param httpClient The [HttpClient] used to perform network requests.
 * @param baseUrl The base URL of the currency rates API.
 * @see RatesRepository
 */
class NetworkRatesRepository(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : RatesRepository {

    /**
     * Fetches currency rate data from the API by making a network request using the specified
     * endpoints and query parameters.
     *
     * @param endPoints A list of endpoints to be appended to the base URL to form the full URL.
     * @param queries A map of query parameters to be included in the request, where each key
     * represents a parameter name and each value represents the parameter value.
     * @return A [CurrencyRatesResponse] containing the currency rate data from the API.
     * @throws IOException If the network request fails due to connectivity issues.
     */
    override suspend fun getRatesData(
        endPoints: List<String>,
        queries: Map<String, String>
    ): CurrencyRatesResponse {
        return fetchRates(
            httpClient = httpClient,
            baseUrl = baseUrl,
            endPoints = endPoints,
            queries = queries
        )
    }
}