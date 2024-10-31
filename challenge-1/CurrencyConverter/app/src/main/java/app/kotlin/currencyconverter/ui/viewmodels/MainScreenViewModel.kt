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
import app.kotlin.currencyconverter.data.UserPreferenceRepository
import app.kotlin.currencyconverter.network.GetMethodEndPoints
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.ParseException

enum class AppDataLoadingState {
    LOADING,
    NO_INTERNET,
    FAILED,
    SUCCESS
}

data class MainScreenUiState(
    val isDarkTheme: Boolean = false,
    val appDataLoadingState: AppDataLoadingState = AppDataLoadingState.LOADING,
    val sourceCurrencyUnit: String = "USD",
    val sourceCurrencyValue: String = "0",
    val targetCurrencyUnit: String = "VND",
    val targetCurrencyValue: String = "0"
)

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class MainScreenViewModel(
    private val ratesRepository: RatesRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<MainScreenUiState> =
        MutableStateFlow(value = MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    private lateinit var currencyUnitsAndRates: Map<String, Double>
    lateinit var currencyUnits: List<String>

    private var currentDecimalPlaces: Int = DEFAULT_DECIMAL_PLACE

    init {
        initTheme()
        initData()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun initData() {
        viewModelScope.launch {
            withContext(IO) {
                try {
                    val endPoints: List<String> = listOf(GetMethodEndPoints.LATEST)
                    val queries: Map<String, String> =
                        mapOf(KEY_ACCESS_KEY_URL to VALUE_ACCESS_KEY_URL)
                    currencyUnitsAndRates = ratesRepository.getRatesData(
                        endPoints = endPoints,
                        queries = queries
                    ).rates
                    currencyUnits = currencyUnitsAndRates.map { pair -> pair.key }
                    updateAppDataLoadingState(newState = AppDataLoadingState.SUCCESS)
                } catch (error: IOException) {
                    updateAppDataLoadingState(newState = AppDataLoadingState.NO_INTERNET)
                } catch (error: Exception) {
                    updateAppDataLoadingState(newState = AppDataLoadingState.FAILED)
                    Log.e("Error", error.toString())
                }
            }
        }
    }

    private fun initTheme() {
        viewModelScope.launch {
            withContext(IO) {
                _uiState.update { currentState ->
                    currentState.copy(isDarkTheme = userPreferenceRepository.isDarkTheme.first())
                }
            }
        }
    }

    private fun updateAppDataLoadingState(newState: AppDataLoadingState) {
        _uiState.update { currentState ->
            currentState.copy(appDataLoadingState = newState)
        }
    }

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

    private fun parseSourceValue(formattedSource: String): String {
        return formattedSource.replace(oldValue = ",", newValue = "")
    }

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

    private fun getCurrentSourceCurrencyRate(): Double {
        val currentSourceCurrencyUnit: String = _uiState.value.sourceCurrencyUnit
        return currencyUnitsAndRates[currentSourceCurrencyUnit] ?: 1.0
    }

    private fun getCurrentTargetCurrencyRate(): Double {
        val currentTargetCurrencyUnit: String = _uiState.value.targetCurrencyUnit
        return currencyUnitsAndRates[currentTargetCurrencyUnit] ?: 1.0
    }

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
            val currentTargetCurrencyRate: Double = getCurrentSourceCurrencyRate()

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

    private val refreshRates: () -> Unit = {
        viewModelScope.launch {
            withContext(IO) {
                try {
                    updateAppDataLoadingState(newState = AppDataLoadingState.LOADING)

                    val endPoints: List<String> = listOf(GetMethodEndPoints.LATEST)
                    val queries: Map<String, String> =
                        mapOf(KEY_ACCESS_KEY_URL to VALUE_ACCESS_KEY_URL)
                    currencyUnitsAndRates = ratesRepository.getRatesData(
                        endPoints = endPoints,
                        queries = queries
                    ).rates
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
                } catch (error: IOException) {
                    updateAppDataLoadingState(newState = AppDataLoadingState.NO_INTERNET)
                } catch (error: Exception) {
                    updateAppDataLoadingState(newState = AppDataLoadingState.FAILED)
                }
            }
        }
    }

    private fun String.multiply(n: Int): String {
        val builder: StringBuilder = StringBuilder()
        for (i in 0 until n) {
            builder.append(this)
        }
        return builder.toString()
    }

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
            } catch (e: ParseException) {
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

    private val toggleTheme: () -> Unit = {
        viewModelScope.launch {
            withContext(IO) {
                val isDarkTheme = _uiState.value.isDarkTheme
                _uiState.update { currentState ->
                    currentState.copy(isDarkTheme = !isDarkTheme)
                }
                userPreferenceRepository.saveThemePreference(isDarkTheme = !isDarkTheme)
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
        appendDotSign,
        toggleTheme
    )

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application: CurrencyConverterApplication =
                    (this[APPLICATION_KEY] as CurrencyConverterApplication)
                val appContainer: AppContainer = application.appContainer
                MainScreenViewModel(
                    ratesRepository = appContainer.ratesRepository,
                    userPreferenceRepository = appContainer.userPreferenceRepository
                )
            }
        }
    }
}