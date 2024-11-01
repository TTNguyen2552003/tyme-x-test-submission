package app.kotlin.currencyconverter.network

import app.kotlin.currencyconverter.data.models.CurrencyRatesResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

/**
 * Fetches currency rates from the API by making a network request to the specified
 * [baseUrl] with a combination of optional [endPoints] and [queries].
 *
 * This function constructs the full API URL by appending [endPoints] to [baseUrl] and
 * applies any [queries] as URL parameters. The response is then parsed into a
 * [CurrencyRatesResponse] object.
 *
 * @param httpClient The [HttpClient] instance used for making the network request.
 * @param baseUrl The base URL of the currency rates API. Defaults to an empty string.
 * @param endPoints A list of endpoint paths to append to [baseUrl] to form the full URL.
 *                  Defaults to an empty list.
 * @param queries A map of query parameters to include in the request URL, where each key
 *                is a parameter name and each value is the parameter value. Defaults to an empty map.
 * @return A [CurrencyRatesResponse] containing the fetched currency rate data.
 * @throws IOException If the network request fails due to connectivity issues.
 * @throws SerializationException If the JSON response cannot be parsed into a [CurrencyRatesResponse].
 */
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