package app.kotlin.currencyconverter.network

import app.kotlin.currencyconverter.data.models.CurrencyRatesResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

suspend fun fetchRates(
    httpClient: HttpClient,
    baseUrl: String = "",
    endPoints: List<String> = emptyList(),
    queries: Map<String, String> = mapOf()
): CurrencyRatesResponse {
    val stringBuilder: StringBuilder = StringBuilder(baseUrl)
    endPoints.forEach { endPoint ->
        stringBuilder.append(endPoint)
    }
    val apiWithEndPoints: String = stringBuilder.toString()

    val jsonRawString = httpClient.get(urlString = apiWithEndPoints) {
        url {
            queries.forEach { (key: String, value: String) ->
                parameters.append(name = key, value = value)
            }
        }
    }.bodyAsText()

    return Json.decodeFromString(string = jsonRawString)
}