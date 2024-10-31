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

data class SharedUiState(
    val isDarkTheme: Boolean = false,
    val tipsScreenShown: Boolean = true
)

class SharedViewModel(
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<SharedUiState> =
        MutableStateFlow(value = SharedUiState())
    val uiState: StateFlow<SharedUiState> = _uiState.asStateFlow()

    init {
        detectShowingTipsScreen()
        initTheme()
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

    private fun detectShowingTipsScreen() {
        viewModelScope.launch {
            withContext(IO) {
                _uiState.update { currentState ->
                    currentState.copy(tipsScreenShown = userPreferenceRepository.tipsScreenShown.first())
                }
            }
        }
    }

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

    val hideTipsScreenPermanently: () -> Unit = {
        viewModelScope.launch {
            withContext(IO) {
                userPreferenceRepository.saveTipsScreenShowingPreference(tipsScreenShown = false)
            }
        }
    }

    companion object {
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