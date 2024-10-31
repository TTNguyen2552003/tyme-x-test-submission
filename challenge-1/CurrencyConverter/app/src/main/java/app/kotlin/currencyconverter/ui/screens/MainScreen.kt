package app.kotlin.currencyconverter.ui.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import app.kotlin.currencyconverter.ui.viewmodels.AppDataLoadingState
import app.kotlin.currencyconverter.ui.viewmodels.MainScreenUiState
import app.kotlin.currencyconverter.ui.viewmodels.MainScreenViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun MainScreen(
    isDarkTheme: Boolean = false,
    toggleTheme: () -> Unit,
    viewModel: MainScreenViewModel = viewModel(factory = MainScreenViewModel.factory)
) {
    val uiState: MainScreenUiState by viewModel.uiState.collectAsState()

    when (uiState.appDataLoadingState) {
        AppDataLoadingState.LOADING -> {
            LoadingScreen(isDarkTheme = isDarkTheme)
        }

        AppDataLoadingState.NO_INTERNET -> {
            NoInternetScreen(
                isDarkTheme = isDarkTheme,
                retryFetchRatesData = viewModel.refreshRates
            )
        }

        AppDataLoadingState.FAILED -> {
            LoadFailedScreen(isDarkTheme = isDarkTheme)
        }

        AppDataLoadingState.SUCCESS -> {
            ConvertScreen(
                isDarkTheme = isDarkTheme,
                toggleTheme = toggleTheme,
                sourceCurrencyUnit = uiState.sourceCurrencyUnit,
                sourceCurrencyValue = uiState.sourceCurrencyValue,
                targetCurrencyUnit = uiState.targetCurrencyUnit,
                targetCurrencyValue = uiState.targetCurrencyValue,
                swapCurrencyUnit = viewModel.swapCurrencyUnit,
                onPressedEvents = viewModel.onPressedEvents,
                updateSourceCurrencyUnit = viewModel.updateSourceCurrencyUnit,
                updateTargetCurrencyUnit = viewModel.updateTargetCurrencyUnit,
                currencyUnits = viewModel.currencyUnits
            )
        }
    }
}
