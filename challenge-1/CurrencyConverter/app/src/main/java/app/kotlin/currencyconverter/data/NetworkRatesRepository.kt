package app.kotlin.currencyconverter.data

import app.kotlin.currencyconverter.data.models.CurrencyRatesResponse
import app.kotlin.currencyconverter.network.fetchRates
import io.ktor.client.HttpClient

class NetworkRatesRepository(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : RatesRepository {
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