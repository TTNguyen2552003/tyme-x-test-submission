package app.kotlin.currencyconverter.network

import app.kotlin.currencyconverter.KEY_ACCESS_KEY_URL
import app.kotlin.currencyconverter.VALUE_ACCESS_KEY_URL
import app.kotlin.currencyconverter.data.models.CurrencyRatesResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

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

private const val BASE_URL = "https://api.exchangeratesapi.io/v1"

suspend fun fetchRates(): CurrencyRatesResponse {
    val jsonRawString = httpClient.get(urlString = "${BASE_URL}${GetMethodEndPoints.LATEST}") {
        url {
            parameters.append(KEY_ACCESS_KEY_URL, VALUE_ACCESS_KEY_URL)
        }
    }.bodyAsText()

    return Json.decodeFromString(string = jsonRawString)
}