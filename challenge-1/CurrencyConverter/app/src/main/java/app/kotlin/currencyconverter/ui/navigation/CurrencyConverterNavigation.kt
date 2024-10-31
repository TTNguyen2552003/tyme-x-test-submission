package app.kotlin.currencyconverter.ui.navigation

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.kotlin.currencyconverter.ui.screens.MainScreen
import app.kotlin.currencyconverter.ui.screens.TipsScreen
import app.kotlin.currencyconverter.ui.viewmodels.SharedUiState
import app.kotlin.currencyconverter.ui.viewmodels.SharedViewModel

enum class Destination(val route: String) {
    TIPS_SCREEN(route = "tips_screen"),
    MAIN_SCREEN(route = "main_screen")
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun CurrencyConverterNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel: SharedViewModel = viewModel(factory = SharedViewModel.factory)
) {
    val uiState: SharedUiState by viewModel.uiState.collectAsState()

    val startDestination: String = if (uiState.tipsScreenShown)
        Destination.TIPS_SCREEN.route
    else
        Destination.MAIN_SCREEN.route
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Destination.TIPS_SCREEN.route) {
            TipsScreen(
                navController = navController,
                isDarkTheme = uiState.isDarkTheme,
                hideTipsScreenPermanently = viewModel.hideTipsScreenPermanently
            )
        }

        composable(route = Destination.MAIN_SCREEN.route) {
            MainScreen(
                isDarkTheme = uiState.isDarkTheme,
                toggleTheme = viewModel.toggleTheme
            )
        }
    }
}