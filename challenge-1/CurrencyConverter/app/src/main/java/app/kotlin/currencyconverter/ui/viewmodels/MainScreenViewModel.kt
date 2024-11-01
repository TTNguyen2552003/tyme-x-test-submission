package app.kotlin.currencyconverter.ui.viewmodels

import android.icu.text.DecimalFormat
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.kotlin.currencyconverter.CurrencyConverterApplication
import app.kotlin.currencyconverter.DEFAULT_DECIMAL_PLACE
import app.kotlin.currencyconverter.DOT_SIGN
import app.kotlin.currencyconverter.KEY_ACCESS_KEY_URL
import app.kotlin.currencyconverter.VALUE_ACCESS_KEY_URL
import app.kotlin.currencyconverter.data.AppContainer
import app.kotlin.currencyconverter.data.RatesRepository
import app.kotlin.currencyconverter.data.models.CurrencyRatesResponse
import app.kotlin.currencyconverter.network.GetMethodEndPoints
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.io.IOException
import java.text.ParseException

/**
 * Represents the different states of data loading in the app.
 *
 * This enum is used to indicate the status of currency data retrieval, which is reflected in the UI
 * to inform users about network issues, success, or ongoing loading processes.
 */
enum class AppDataLoadingState {
    /** Indicates that data is currently being loaded. */
    LOADING,

    /** Indicates that no internet connection is available. */
    NO_INTERNET,

    /** Indicates that the data loading process has failed due to an error. */
    FAILED,

    /** Indicates that data has been successfully loaded. */
    SUCCESS
}

/**
 * Holds the UI state data for the main screen of the app.
 *
 * This data class stores the necessary state information for displaying currency conversion data,
 * including the source and target currency units and values, as well as the loading status.
 *
 * @property appDataLoadingState The current data loading state, which indicates if data is loading,
 *                               has failed, succeeded, or if there is no internet.
 * @property sourceCurrencyUnit The unit of the source currency (default is "USD").
 * @property sourceCurrencyValue The value of the source currency entered by the user.
 * @property targetCurrencyUnit The unit of the target currency (default is "VND").
 * @property targetCurrencyValue The converted value of the target currency.
 * @property lastRatesUpdatingDate A string representing the last update date of the currency rates.
 */
data class MainScreenUiState(
    val appDataLoadingState: AppDataLoadingState = AppDataLoadingState.LOADING,
    val sourceCurrencyUnit: String = "USD",
    val sourceCurrencyValue: String = "0",
    val targetCurrencyUnit: String = "VND",
    val targetCurrencyValue: String = "0",
    val lastRatesUpdatingDate: String = "loading..."
)


/**
 * ViewModel responsible for managing the main currency converter screen's UI state and business logic.
 * Handles currency conversion calculations, number formatting, and real-time rate updates.
 *
 * @property ratesRepository Repository for fetching currency exchange rates
 * @constructor Creates MainScreenViewModel with the specified RatesRepository
 */
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class MainScreenViewModel(
    private val ratesRepository: RatesRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MainScreenUiState> =
        MutableStateFlow(value = MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    private lateinit var currencyUnitsAndRates: Map<String, Double>
    lateinit var currencyUnits: List<String>

    private var currentDecimalPlaces: Int = DEFAULT_DECIMAL_PLACE

    init {
        initData()
    }

    /**
     * Initializes currency rate data by fetching latest rates from the repository.
     * Updates UI state based on the API response or error conditions.
     *
     * @throws IOException If network connection fails
     * @throws TimeoutCancellationException If request exceeds timeout duration
     * @RequiresExtension Requires Android S (API level 31) or higher
     */
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun initData() {
        viewModelScope.launch {
            withContext(IO) {
                try {
                    val endPoints: List<String> = listOf(GetMethodEndPoints.LATEST)
                    val queries: Map<String, String> =
                        mapOf(KEY_ACCESS_KEY_URL to VALUE_ACCESS_KEY_URL)
                    val currencyRatesResponse: CurrencyRatesResponse

                    withTimeout(timeMillis = TIMEOUT_REQUEST_DURATION) {
                        currencyRatesResponse = ratesRepository.getRatesData(
                            endPoints = endPoints,
                            queries = queries
                        )
                    }

                    currencyUnitsAndRates = currencyRatesResponse.rates
                    currencyUnits = currencyUnitsAndRates.map { pair -> pair.key }
                    _uiState.update { currentState->currentState.copy(
                        appDataLoadingState = AppDataLoadingState.SUCCESS,
                        lastRatesUpdatingDate = currencyRatesResponse.date
                    ) }
                } catch (exception: IOException) {
                    updateAppDataLoadingState(newState = AppDataLoadingState.NO_INTERNET)
                } catch (exception: TimeoutCancellationException) {
                    updateAppDataLoadingState(newState = AppDataLoadingState.NO_INTERNET)
                } catch (exception: Exception) {
                    updateAppDataLoadingState(newState = AppDataLoadingState.FAILED)
                    Log.e("Error", exception.toString())
                }
            }
        }
    }

    /**
    * Updates the app's data loading state in the UI
    * @param newState New state to be set
    */
    private fun updateAppDataLoadingState(newState: AppDataLoadingState) {
        _uiState.update { currentState ->
            currentState.copy(appDataLoadingState = newState)
        }
    }

    /**
     * Formats the source currency value with proper thousand separators and decimal point
     * @param sourceValue Raw source value to format
     * @return Formatted string representation of the value
     */
    private fun formatSourceCurrencyValue(sourceValue: String): String {
        val integerPartAndFractionalPart = sourceValue.split(".")
        val integerPartFormatter = DecimalFormat("#,###")
        val formattedIntegerPart =
            integerPartFormatter.format(integerPartAndFractionalPart[0].toInt())
        return if (integerPartAndFractionalPart.size > 1)
            "$formattedIntegerPart.${integerPartAndFractionalPart[1]}"
        else
            formattedIntegerPart
    }

    /**
     * Removes formatting characters from the source value
     * @param formattedSource Formatted source string
     * @return Raw string without formatting characters
     */
    private fun parseSourceValue(formattedSource: String): String {
        return formattedSource.replace(oldValue = ",", newValue = "")
    }

    /**
     * Appends a character to the source currency value and updates related calculations
     * @param char Character to append
     */
    private fun appendSourceCurrencyValue(char: Char) {
        val newSourceCurrencyValue: String

        val currentSourceCurrencyValue: String = _uiState.value.sourceCurrencyValue
        if (currentSourceCurrencyValue == "0" && char in '0'..'9') {
            newSourceCurrencyValue = char.toString()
        } else {
            val unformattedSourceCurrencyValue: String = parseSourceValue(
                formattedSource = currentSourceCurrencyValue
            )
            newSourceCurrencyValue = "${unformattedSourceCurrencyValue}${char}"
        }

        val currentSourceCurrencyRate: Double = getCurrentSourceCurrencyRate()
        val currentTargetCurrencyRate: Double = getCurrentTargetCurrencyRate()

        val newTargetCurrencyValue = convertValue(
            sourceCurrencyRate = currentSourceCurrencyRate,
            targetCurrencyRate = currentTargetCurrencyRate,
            sourceCurrencyValue = newSourceCurrencyValue
        )

        _uiState.update { currentState ->
            currentState.copy(
                sourceCurrencyValue = formatSourceCurrencyValue(sourceValue = newSourceCurrencyValue),
                targetCurrencyValue = newTargetCurrencyValue
            )
        }
    }

    /**
     * Lambda function to swap source and target currency units and recalculate conversions
     */
    val swapCurrencyUnit: () -> Unit = {
        val newSourceCurrencyRate: Double = getCurrentTargetCurrencyRate()
        val newTargetCurrencyRate: Double = getCurrentSourceCurrencyRate()

        val currentSourceCurrencyValue: String = _uiState.value.sourceCurrencyValue

        val newTargetCurrencyValue = convertValue(
            sourceCurrencyRate = newSourceCurrencyRate,
            targetCurrencyRate = newTargetCurrencyRate,
            sourceCurrencyValue = parseSourceValue(formattedSource = currentSourceCurrencyValue)
        )

        _uiState.update { currentState ->
            currentState.copy(
                sourceCurrencyUnit = currentState.targetCurrencyUnit,
                targetCurrencyUnit = currentState.sourceCurrencyUnit,
                targetCurrencyValue = newTargetCurrencyValue
            )
        }
    }

    /**
    * Lambda function to update the source currency unit and recalculate the conversion
    * @param newUnit New currency unit to set as source
    */
    val updateSourceCurrencyUnit: (String) -> Unit = { newUnit: String ->
        val newSourceCurrencyRate: Double = currencyUnitsAndRates[newUnit] ?: 1.0
        val currentTargetCurrencyRate: Double = getCurrentTargetCurrencyRate()

        val currentSourceCurrencyValue = _uiState.value.sourceCurrencyValue

        val newTargetCurrencyValue = convertValue(
            sourceCurrencyRate = newSourceCurrencyRate,
            targetCurrencyRate = currentTargetCurrencyRate,
            sourceCurrencyValue = parseSourceValue(formattedSource = currentSourceCurrencyValue)
        )

        _uiState.update { currentState ->
            currentState.copy(
                sourceCurrencyUnit = newUnit,
                targetCurrencyValue = newTargetCurrencyValue
            )
        }
    }

    /**
     * Lambda function to update the target currency unit and recalculate the conversion
     * @param newUnit New currency unit to set as target
     */
    val updateTargetCurrencyUnit: (String) -> Unit = { newUnit: String ->
        val currentSourceCurrencyRate: Double = getCurrentSourceCurrencyRate()
        val newTargetCurrencyRate: Double = currencyUnitsAndRates[newUnit] ?: 1.0

        val currentSourceCurrencyValue: String = _uiState.value.sourceCurrencyValue

        val newTargetCurrencyValue: String = convertValue(
            sourceCurrencyRate = currentSourceCurrencyRate,
            targetCurrencyRate = newTargetCurrencyRate,
            sourceCurrencyValue = parseSourceValue(formattedSource = currentSourceCurrencyValue)
        )

        _uiState.update { currentState ->
            currentState.copy(
                targetCurrencyUnit = newUnit,
                targetCurrencyValue = newTargetCurrencyValue
            )
        }
    }

    /**
     * Retrieves the current source currency exchange rate
     * @return Current source currency rate or 1.0 if not found
     */
    private fun getCurrentSourceCurrencyRate(): Double {
        val currentSourceCurrencyUnit: String = _uiState.value.sourceCurrencyUnit
        return currencyUnitsAndRates[currentSourceCurrencyUnit] ?: 1.0
    }

    /**
     * Retrieves the current target currency exchange rate
     * @return Current target currency rate or 1.0 if not found
     */
    private fun getCurrentTargetCurrencyRate(): Double {
        val currentTargetCurrencyUnit: String = _uiState.value.targetCurrencyUnit
        return currencyUnitsAndRates[currentTargetCurrencyUnit] ?: 1.0
    }

    /**
     * Converts a value from source currency to target currency
     * @param sourceCurrencyRate Source currency exchange rate
     * @param targetCurrencyRate Target currency exchange rate
     * @param sourceCurrencyValue Value to convert
     * @return Formatted string of converted value
     */
    private fun convertValue(
        sourceCurrencyRate: Double,
        targetCurrencyRate: Double,
        sourceCurrencyValue: String,
    ): String {
        resetDecimalPlaces()

        val newTargetCurrencyValueInDouble: Double =
            sourceCurrencyValue.toDouble() / sourceCurrencyRate * targetCurrencyRate

        val pattern = "#,###.${"#".multiply(n = currentDecimalPlaces)}"
        val decimalFormatter = DecimalFormat(pattern)

        return decimalFormatter.format(newTargetCurrencyValueInDouble)
    }

    private fun resetDecimalPlaces() {
        currentDecimalPlaces = DEFAULT_DECIMAL_PLACE
    }

    private val appendDotSign: () -> Unit = {
        fun canAppendDotSign(value: String): Boolean {
            return !value.contains(char = DOT_SIGN)
        }

        val currentSourceCurrencyValue: String = _uiState.value.sourceCurrencyValue
        if (canAppendDotSign(currentSourceCurrencyValue)) {
            _uiState.update { currentState ->
                currentState.copy(sourceCurrencyValue = "${currentState.sourceCurrencyValue}${DOT_SIGN}")
            }
        }
    }

    private val clear: () -> Unit = {
        _uiState.update { currentState ->
            currentState.copy(
                sourceCurrencyValue = "0",
                targetCurrencyValue = "0"
            )
        }
    }

    private val delete: () -> Unit = {
        val newSourceCurrencyValue: String = parseSourceValue(
            formattedSource = _uiState.value.sourceCurrencyValue.dropLast(n = 1)
        )

        if (newSourceCurrencyValue.isEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    sourceCurrencyValue = "0",
                    targetCurrencyValue = "0"
                )
            }
        } else {
            val currentSourceCurrencyRate: Double = getCurrentSourceCurrencyRate()
            val currentTargetCurrencyRate: Double = getCurrentTargetCurrencyRate()

            val newTargetCurrencyValue: String = convertValue(
                sourceCurrencyRate = currentSourceCurrencyRate,
                targetCurrencyRate = currentTargetCurrencyRate,
                sourceCurrencyValue = newSourceCurrencyValue
            )

            _uiState.update { currentState ->
                currentState.copy(
                    sourceCurrencyValue = formatSourceCurrencyValue(sourceValue = newSourceCurrencyValue),
                    targetCurrencyValue = newTargetCurrencyValue
                )
            }
        }
    }

    val refreshRates: () -> Unit = {
        viewModelScope.launch {
            withContext(IO) {
                try {
                    updateAppDataLoadingState(newState = AppDataLoadingState.LOADING)

                    val endPoints: List<String> = listOf(GetMethodEndPoints.LATEST)
                    val queries: Map<String, String> =
                        mapOf(KEY_ACCESS_KEY_URL to VALUE_ACCESS_KEY_URL)
                    val currencyRatesResponse: CurrencyRatesResponse

                    withTimeout(timeMillis = TIMEOUT_REQUEST_DURATION) {
                        currencyRatesResponse = ratesRepository.getRatesData(
                            endPoints = endPoints,
                            queries = queries
                        )
                    }

                    currencyUnitsAndRates = currencyRatesResponse.rates
                    currencyUnits = currencyUnitsAndRates.map { pair -> pair.key }
                    updateAppDataLoadingState(newState = AppDataLoadingState.SUCCESS)

                    val currentSourceCurrencyRate: Double = getCurrentSourceCurrencyRate()
                    val currentTargetCurrencyRate: Double = getCurrentTargetCurrencyRate()

                    val currentSourceCurrencyValue: String = _uiState.value.sourceCurrencyValue

                    val newTargetCurrencyValue: String = convertValue(
                        sourceCurrencyRate = currentSourceCurrencyRate,
                        targetCurrencyRate = currentTargetCurrencyRate,
                        sourceCurrencyValue = parseSourceValue(formattedSource = currentSourceCurrencyValue)
                    )

                    _uiState.update { currentState ->
                        currentState.copy(targetCurrencyValue = newTargetCurrencyValue)
                    }
                } catch (exception: IOException) {
                    updateAppDataLoadingState(newState = AppDataLoadingState.NO_INTERNET)
                } catch (exception: TimeoutCancellationException) {
                    updateAppDataLoadingState(newState = AppDataLoadingState.NO_INTERNET)
                } catch (exception: Exception) {
                    updateAppDataLoadingState(newState = AppDataLoadingState.FAILED)
                    Log.e("Error", exception.toString())
                }
            }
        }
    }

    /**
     * Extension function to multiply a string n times
     * @param n Number of times to multiply the string
     * @return Resulting multiplied string
     */
    private fun String.multiply(n: Int): String {
        val builder: StringBuilder = StringBuilder()
        for (i in 0 until n) {
            builder.append(this)
        }
        return builder.toString()
    }

    /**
     * Rounds a number to specified decimal places with proper formatting
     * @param number Number to round
     * @param decimalPlaces Number of decimal places to round to
     * @return Formatted string representation of rounded number
     */
    private fun roundNumber(number: Double, decimalPlaces: Int): String {
        val pattern = if (decimalPlaces == 0)
            "#,###"
        else
            "#,###.${"#".multiply(decimalPlaces)}"
        val decimalFormatter = DecimalFormat(pattern)

        return decimalFormatter.format(number)
    }

    private val decreaseDecimal: () -> Unit = {
        if (currentDecimalPlaces > 0) {
            val currentTargetCurrencyValue: String = _uiState.value.targetCurrencyValue

            val pattern = "#,###.${"#".multiply(currentDecimalPlaces)}"
            val decimalFormat = DecimalFormat(pattern)
            val currentTargetCurrencyValueInDouble: Double? = try {
                decimalFormat.parse(currentTargetCurrencyValue)?.toDouble()
            } catch (exception: ParseException) {
                null
            }

            currentDecimalPlaces--

            val newTargetCurrencyValue: String = roundNumber(
                number = currentTargetCurrencyValueInDouble ?: 0.0,
                decimalPlaces = currentDecimalPlaces
            )

            _uiState.update { currentState ->
                currentState.copy(targetCurrencyValue = newTargetCurrencyValue)
            }
        }
    }

    val onPressedEvents: List<() -> Unit> = listOf(
        { appendSourceCurrencyValue(char = '7') },
        { appendSourceCurrencyValue(char = '8') },
        { appendSourceCurrencyValue(char = '9') },
        clear,
        { appendSourceCurrencyValue(char = '4') },
        { appendSourceCurrencyValue(char = '5') },
        { appendSourceCurrencyValue(char = '6') },
        delete,
        { appendSourceCurrencyValue(char = '1') },
        { appendSourceCurrencyValue(char = '2') },
        { appendSourceCurrencyValue(char = '3') },
        refreshRates,
        decreaseDecimal,
        { appendSourceCurrencyValue(char = '0') },
        appendDotSign
    )

    companion object {
        /**
         * Factory for creating MainScreenViewModel instances with proper dependency injection
         */
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application: CurrencyConverterApplication =
                    (this[APPLICATION_KEY] as CurrencyConverterApplication)
                val appContainer: AppContainer = application.appContainer
                MainScreenViewModel(ratesRepository = appContainer.ratesRepository)

            }
        }

        /**
         * Timeout duration for API requests in milliseconds
         */
        const val TIMEOUT_REQUEST_DURATION = 10000L
    }
}