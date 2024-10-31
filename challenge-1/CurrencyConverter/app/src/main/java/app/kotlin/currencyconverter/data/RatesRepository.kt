package app.kotlin.currencyconverter.data

import app.kotlin.currencyconverter.data.models.CurrencyRatesResponse

interface RatesRepository {
    suspend fun getRatesData(
        endPoints: List<String>,
        queries: Map<String, String>
    ): CurrencyRatesResponse
}