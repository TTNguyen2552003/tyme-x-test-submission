package app.kotlin.currencyconverter.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyRatesResponse(
    val success: Boolean,
    @SerialName(value = "timestamp") val epochTimeApiCalled: Long,
    @SerialName(value = "base") val currencyBaseUnit: String,
    val date: String,
    val rates: Map<String, Double>
)