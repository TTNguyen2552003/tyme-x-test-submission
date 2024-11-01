package app.kotlin.currencyconverter.data

import app.kotlin.currencyconverter.data.models.CurrencyRatesResponse

/**
 * A repository interface for fetching currency exchange rate data.
 *
 * Implementations of this interface are responsible for providing currency rate data
 * based on specified API endpoints and query parameters.
 */
interface RatesRepository {

    /**
     * Fetches currency rate data from a data source, such as a network API, using the specified
     * endpoints and query parameters.
     *
     * @param endPoints A list of endpoint path segments that will be appended to the base URL to form the full URL.
     *                  Each entry in the list represents a part of the URL path.
     * @param queries A map of query parameters where each key represents the parameter name
     *                and each value represents the corresponding parameter value.
     * @return A [CurrencyRatesResponse] containing the requested currency rate data.
     * @throws IOException If the data retrieval fails due to a network or data source error.
     */
    suspend fun getRatesData(
        endPoints: List<String>,
        queries: Map<String, String>
    ): CurrencyRatesResponse
}
