package app.kotlin.currencyconverter.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the response of currency rates from an API call.
 *
 * This response includes whether the call was successful, the timestamp of the call,
 * the base currency unit, the date of the rates, and a map of currency exchange rates.
 *
 * @property success Indicates if the API call was successful.
 * @property epochTimeApiCalled The Unix timestamp (in seconds) representing when the API was called.
 * @property currencyBaseUnit The base currency unit used in the rates, as a 3-letter currency code (e.g., "USD").
 * @property date The date the currency rates apply to, in "YYYY-MM-DD" format.
 * @property rates A map containing currency codes as keys and their corresponding exchange rates as values.
 */
@Serializable
data class CurrencyRatesResponse(
    val success: Boolean,

    @SerialName(value = "timestamp")
    val epochTimeApiCalled: Long,

    @SerialName(value = "base")
    val currencyBaseUnit: String,

    val date: String,

    val rates: Map<String, Double>
)
