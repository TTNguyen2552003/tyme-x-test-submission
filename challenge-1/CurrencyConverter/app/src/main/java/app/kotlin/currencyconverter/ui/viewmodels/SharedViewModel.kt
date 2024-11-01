package app.kotlin.currencyconverter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.kotlin.currencyconverter.CurrencyConverterApplication
import app.kotlin.currencyconverter.data.AppContainer
import app.kotlin.currencyconverter.data.UserPreferenceRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Data class representing the UI state for shared components across the application.
 *
 * @property isDarkTheme Boolean flag indicating whether dark theme is enabled
 * @property tipsScreenShown Boolean flag indicating whether the tips screen should be displayed
 */
data class SharedUiState(
    val isDarkTheme: Boolean = false,
    val tipsScreenShown: Boolean = true
)

/**
 * ViewModel responsible for managing shared UI state and user preferences across the application.
 * This includes theme management and tips screen visibility preferences.
 *
 * @property userPreferenceRepository Repository handling user preference storage and retrieval
 * @constructor Creates a SharedViewModel with the specified UserPreferenceRepository
 */
class SharedViewModel(
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    /**
     * Backing property for UI state updates
     */
    private val _uiState: MutableStateFlow<SharedUiState> =
        MutableStateFlow(value = SharedUiState())

    /**
     * Public immutable StateFlow exposing the current UI state
     */
    val uiState: StateFlow<SharedUiState> = _uiState.asStateFlow()

    /**
     * Initializes the ViewModel by detecting tips screen visibility and theme preferences
     */
    init {
        detectShowingTipsScreen()
        initTheme()
    }

    /**
     * Initializes the theme preference by reading from the UserPreferenceRepository
     * and updating the UI state accordingly.
     */
    private fun initTheme() {
        viewModelScope.launch {
            withContext(IO) {
                _uiState.update { currentState ->
                    currentState.copy(isDarkTheme = userPreferenceRepository.isDarkTheme.first())
                }
            }
        }
    }

    /**
     * Detects whether the tips screen should be shown based on user preferences
     * and updates the UI state accordingly.
     */
    private fun detectShowingTipsScreen() {
        viewModelScope.launch {
            withContext(IO) {
                _uiState.update { currentState ->
                    currentState.copy(tipsScreenShown = userPreferenceRepository.tipsScreenShown.first())
                }
            }
        }
    }

    /**
     * Lambda function to toggle the theme between light and dark mode.
     * Updates both the UI state and persists the preference.
     */
    val toggleTheme: () -> Unit = {
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

    /**
     * Lambda function to permanently hide the tips screen.
     * Persists this preference to the UserPreferenceRepository.
     */
    val hideTipsScreenPermanently: () -> Unit = {
        viewModelScope.launch {
            withContext(IO) {
                userPreferenceRepository.saveTipsScreenShowingPreference(tipsScreenShown = false)
            }
        }
    }

    /**
     * Companion object containing factory methods for creating SharedViewModel instances.
     */
    companion object {
        /**
         * Factory for creating SharedViewModel instances with proper dependency injection.
         * Uses the application container to provide required dependencies.
         */
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application: CurrencyConverterApplication =
                    (this[APPLICATION_KEY] as CurrencyConverterApplication)
                val appContainer: AppContainer = application.appContainer
                SharedViewModel(userPreferenceRepository = appContainer.userPreferenceRepository)
            }
        }
    }
}